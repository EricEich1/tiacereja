export interface TipoEvento {
  id?: number;
  nome: string;
  descricao?: string;
  capacidadeMinima?: number;
  capacidadeMaxima?: number;
  ativo: boolean;
  dataCriacao?: Date;
}
