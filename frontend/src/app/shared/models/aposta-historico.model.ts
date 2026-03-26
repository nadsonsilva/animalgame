export interface ApostaHistorico {
  id: number;
  usuarioId: number;
  nomeUsuario: string;
  grupoAnimal: number;
  nomeAnimal: string;
  valorApostado: number;
  dataHora: string;
  vencedora: boolean;
  premio: number;
}