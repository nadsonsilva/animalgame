export interface ApostaResponse {
  numeroEscolhido: number;
  animalEscolhido: string;
  numeroSorteado: number;
  animalSorteado: string;
  ganhou: boolean;
  valorGanho: number;
  valorApostado: number;
  tipoAposta?: string;
  numeroApostado?: string | null;
  segundoNumero?: string | null;
  milharSorteada?: string | null;
  premiosSorteados?: string[];
  resultadoComparado?: string | null;
}
