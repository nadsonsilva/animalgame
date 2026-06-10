import { ApostaResponse } from './aposta-response.model';

export interface SorteioResponse {
  grupoSorteado: number | null;
  mensagem: string;
  premios: string[];
  dezenas: string[];
  grupos: number[];
  apostasProcessadas?: ApostaResponse[];
}
