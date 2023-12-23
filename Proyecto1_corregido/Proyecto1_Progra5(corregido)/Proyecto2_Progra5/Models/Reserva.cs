using System.ComponentModel.DataAnnotations.Schema;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel;

namespace Proyecto2_Progra5.Models
{
    public class Reserva
    {
        [Key]
        [DatabaseGenerated(DatabaseGeneratedOption.Identity)]
        [DisplayName("Id")]
        public int Id { get; set; }

        [Required]
        [DisplayName("Fecha de Reserva")]
        public DateTime FechaReserva { get; set; }

        [Required]
        [DisplayName("Fecha Final de Pago")]
        public DateTime FechaFinalPago { get; set; }

        [Required]
        public float Monto { get; set; }

        [ForeignKey("Usuarios")]
        public int UsuariosId { get; set; }
        public Usuarios Usuarios { get; set; }
    }
}
