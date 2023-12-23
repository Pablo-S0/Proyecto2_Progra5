using System.ComponentModel.DataAnnotations.Schema;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel;

namespace Proyecto2_Progra5.Models
{
    public class Usuarios
    {
        [Key]
        [DatabaseGenerated(DatabaseGeneratedOption.Identity)]
        [DisplayName("Id")]
        public int Id { get; set; }

        [Required]
        public string Nombre { get; set; }

        [Required]
        public string Correo { get; set;}

        [Required]
        public string Clave { get; set; }

        [Required]
        public string Cedula { get; set; }

        [Required]
        [MaxLength(100)]
        public string Telefono { get; set; }


        [ForeignKey("Rol")]
        public int RolId { get; set;}
        public Rol Rol { get; set;}

        [InverseProperty("Usuarios")]
        public List<Reserva> Reservas { get; set; }
    }
}
