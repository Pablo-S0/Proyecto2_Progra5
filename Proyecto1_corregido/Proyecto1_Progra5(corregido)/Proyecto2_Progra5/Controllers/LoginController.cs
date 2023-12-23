using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using Proyecto2_Progra5.Data;
using Proyecto2_Progra5.Models;
using System.Net;
using Newtonsoft.Json;
using Microsoft.AspNetCore.Authentication.Cookies;
using Microsoft.AspNetCore.Authentication;
using System.Security.Claims;

namespace Proyecto2_Progra5.Controllers
{
    public class LoginController : Controller
    {
        private readonly AppDbContext _context;

        public LoginController(AppDbContext context)
        {
            _context = context;
        }
        public async Task<Usuarios> GetUsuario(string correo, string clave)
        {
            Usuarios usuario_encontrado = await _context.Usuarios.Where(u => u.Correo == correo && u.Clave == clave)
                .FirstOrDefaultAsync();

            return usuario_encontrado;
        }

        public async Task<Usuarios> SaveUsuario(Usuarios modelo)
        {
            _context.Usuarios.Add(modelo);
            await _context.SaveChangesAsync();
            return modelo;
        }

        public IActionResult Registrarse()
        {
            return View();
        }

        [HttpPost]
        public async Task<IActionResult> Registrarse(Usuarios modelo)
        {
            //modelo.Clave = Utilidades.EncriptarClave(modelo.Clave);

            Usuarios usuario_creado = await SaveUsuario(modelo);

            if (usuario_creado.Id > 0)
                return RedirectToAction("Login", "Login");

            ViewData["Mensaje"] = "No se pudo crear el usuario";
            return View();
        }

        public IActionResult Login()
        {
            return View();
        }

        [HttpPost]
        public async Task<IActionResult> Login(string correo, string clave)
        {
            Usuarios usuario_encontrado = await GetUsuario(correo, clave); //(correo, Utilidades.EncriptarClave(clave))

            if (usuario_encontrado == null)
            {
                ViewData["Mensaje"] = "No se encontraron coincidencias";
                return View();
            }

            List<Claim> claims = new List<Claim>()
            {
               new Claim("Cedula", usuario_encontrado.Cedula),
                new Claim(ClaimTypes.Name, usuario_encontrado.Nombre),
                new Claim(ClaimTypes.Role, usuario_encontrado.RolId.ToString())
            };

            ClaimsIdentity claimsIdentity = new ClaimsIdentity(claims, CookieAuthenticationDefaults.AuthenticationScheme);
            AuthenticationProperties properties = new AuthenticationProperties()
            {
                AllowRefresh = true
            };

            await HttpContext.SignInAsync(
                CookieAuthenticationDefaults.AuthenticationScheme,
                new ClaimsPrincipal(claimsIdentity),
                properties
                );

            return RedirectToAction("Index", "Home");
        }
    }
}


