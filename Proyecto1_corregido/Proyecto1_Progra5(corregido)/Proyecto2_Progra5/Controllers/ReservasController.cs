using System;
using System.Collections.Generic;
using System.Linq;
using System.Security.Claims;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.Mvc.Rendering;
using Microsoft.EntityFrameworkCore;
using Proyecto2_Progra5.Data;
using Proyecto2_Progra5.Models;

namespace Proyecto2_Progra5.Controllers
{
    public class ReservasController : Controller
    {
        private readonly AppDbContext _context;

        public ReservasController(AppDbContext context)
        {
            _context = context;
        }

        public void Validar()
        {
            ClaimsPrincipal claimuser = HttpContext.User;
            string cedulaUsuario = "";
            string nombreUsuario = "";
            int rol = 0;
            if (claimuser.Identity.IsAuthenticated)
            {
                cedulaUsuario = claimuser.Claims.Where(c => c.Type == "Cedula")
            .Select(c => c.Value).SingleOrDefault();
                nombreUsuario = claimuser.Claims.Where(c => c.Type == ClaimTypes.Name)
                    .Select(c => c.Value).SingleOrDefault();
                rol = int.Parse(claimuser.Claims.Where(c => c.Type == ClaimTypes.Role)
                    .Select(c => c.Value).SingleOrDefault());
            }
            ViewData["cedulaUsuario"] = cedulaUsuario;
            ViewData["nombreUsuario"] = nombreUsuario;
            ViewData["rol"] = rol.ToString();
        }

        // GET: Reservas
        [Authorize]
        public async Task<IActionResult> Index()
        {
            Validar();
            
                IQueryable<Reserva> reservasQuery = _context.Reservas.Include(r => r.Usuarios);
            
            if (ViewData["rol"] == "2")
            {
                // Si el rol es 2, filtrar las reservas por el usuario autenticado
                string cedulaaUsuario = ViewData["cedulaUsuario"].ToString(); // Obteniendo la cédula del usuario autenticado
                reservasQuery = reservasQuery.Where(r => r.Usuarios.Cedula == cedulaaUsuario);
            }

            return View(await reservasQuery.ToListAsync());
        }


        // GET: Reservas/Details/5
        [Authorize]
        public async Task<IActionResult> Details(int? id)
        {
            Validar();
            if (id == null || _context.Reservas == null)
            {
                return NotFound();
            }

            var reserva = await _context.Reservas
                .Include(r => r.Usuarios)
                .FirstOrDefaultAsync(m => m.Id == id);
            if (reserva == null)
            {
                return NotFound();
            }

            return View(reserva);
        }

        // GET: Reservas/Create
        [Authorize]
        public IActionResult Create()
        {
            Validar();
            ViewData["UsuariosId"] = new SelectList(_context.Usuarios, "Id", "Cedula");
            return View();
        }

        // POST: Reservas/Create
        // To protect from overposting attacks, enable the specific properties you want to bind to.
        // For more details, see http://go.microsoft.com/fwlink/?LinkId=317598.
        [HttpPost]
        [ValidateAntiForgeryToken]
        public async Task<IActionResult> Create([Bind("Id,FechaReserva,FechaFinalPago,Monto,UsuariosId")] Reserva reserva)
        {
            
                _context.Add(reserva);
                await _context.SaveChangesAsync();
                return RedirectToAction(nameof(Index));
            
            ViewData["UsuariosId"] = new SelectList(_context.Usuarios, "Id", "Cedula", reserva.UsuariosId);
            return View(reserva);
        }

        // GET: Reservas/Edit/5
        [Authorize]
        public async Task<IActionResult> Edit(int? id)
        {
            Validar();
            if (id == null || _context.Reservas == null)
            {
                return NotFound();
            }

            var reserva = await _context.Reservas.FindAsync(id);
            if (reserva == null)
            {
                return NotFound();
            }
            ViewData["UsuariosId"] = new SelectList(_context.Usuarios, "Id", "Cedula", reserva.UsuariosId);
            return View(reserva);
        }

        // POST: Reservas/Edit/5
        // To protect from overposting attacks, enable the specific properties you want to bind to.
        // For more details, see http://go.microsoft.com/fwlink/?LinkId=317598.
        [HttpPost]
        [ValidateAntiForgeryToken]
        public async Task<IActionResult> Edit(int id, [Bind("Id,FechaReserva,FechaFinalPago,Monto,UsuariosId")] Reserva reserva)
        {
            if (id != reserva.Id)
            {
                return NotFound();
            }

            
                try
                {
                    _context.Update(reserva);
                    await _context.SaveChangesAsync();
                }
                catch (DbUpdateConcurrencyException)
                {
                    if (!ReservaExists(reserva.Id))
                    {
                        return NotFound();
                    }
                    else
                    {
                        throw;
                    }
                }
                return RedirectToAction(nameof(Index));
            
            ViewData["UsuariosId"] = new SelectList(_context.Usuarios, "Id", "Cedula", reserva.UsuariosId);
            return View(reserva);
        }

        // GET: Reservas/Delete/5
        [Authorize]
        public async Task<IActionResult> Delete(int? id)
        {
            Validar();
            if (id == null || _context.Reservas == null)
            {
                return NotFound();
            }

            var reserva = await _context.Reservas
                .Include(r => r.Usuarios)
                .FirstOrDefaultAsync(m => m.Id == id);
            if (reserva == null)
            {
                return NotFound();
            }

            return View(reserva);
        }

        // POST: Reservas/Delete/5
        [HttpPost, ActionName("Delete")]
        [ValidateAntiForgeryToken]
        public async Task<IActionResult> DeleteConfirmed(int id)
        {
            if (_context.Reservas == null)
            {
                return Problem("Entity set 'AppDbContext.Reservas'  is null.");
            }
            var reserva = await _context.Reservas.FindAsync(id);
            if (reserva != null)
            {
                _context.Reservas.Remove(reserva);
            }
            
            await _context.SaveChangesAsync();
            return RedirectToAction(nameof(Index));
        }

        private bool ReservaExists(int id)
        {
          return (_context.Reservas?.Any(e => e.Id == id)).GetValueOrDefault();
        }
    }
}
