export interface ApostaRequest {
  usuarioId: number;
  grupoAnimal?: number | null;
  valor: number;
  tipoAposta?: string;
  numeroApostado?: string | null;
  segundoNumero?: string | null;
}
