using Microsoft.EntityFrameworkCore;
using Proyecto2_Progra5.Models;
using System.Collections.Generic;

namespace Proyecto2_Progra5.Data
{
    public class AppDbContext : DbContext
    {
        public AppDbContext(DbContextOptions<AppDbContext> options) : base(options)
        {
        }

        public DbSet<Rol> Roles { get; set; }
        public DbSet<Usuarios> Usuarios { get; set; }
        public DbSet<Reserva> Reservas { get; set; }


    }
}
