CREATE TABLE Colaborador (
  id SMALLINT  NOT NULL   IDENTITY ,
  nome VARCHAR(25)  NOT NULL  ,
  sobrenome VARCHAR(35)    ,
  apelido VARCHAR(15)  NOT NULL  ,
  email VARCHAR(50)  NOT NULL  ,
  emailAlternativo VARCHAR(50)    ,
  usuario VARCHAR(16)  NOT NULL  ,
  senha VARCHAR(128)  NOT NULL  ,
  tipoUsuario TINYINT  NOT NULL  ,
  tipoAcesso TINYINT  NOT NULL  ,
  flagAtivo BIT    ,
  dataUltLogin DATETIME    ,
  diaAniversario TINYINT    ,
  mesAniversario TINYINT    ,
  ramal VARCHAR(15)    ,
  telResid VARCHAR(15)    ,
  telCelular VARCHAR(15)    ,
  flagTrabSegunda BIT    ,
  flagTrabTerca BIT    ,
  flagTrabQuarta BIT    ,
  flagTrabQuinta BIT    ,
  flagTrabSexta BIT    ,
  flagTrabSabado BIT    ,
  flagTrabDomingo BIT    ,
  valorCustoHora VARCHAR(128)    ,
  valorCustoMes VARCHAR(128)    ,
  foto LONGBLOB      ,
PRIMARY KEY(id)  );
GO


CREATE UNIQUE INDEX Colaborador_chaveUnica ON Colaborador (usuario);
GO




CREATE TABLE Feriado (
  id INTEGER  NOT NULL   IDENTITY ,
  dataFeriado DATETIME  NOT NULL  ,
  nome VARCHAR(24)    ,
  repetir INTEGER      ,
PRIMARY KEY(id)  );
GO


CREATE UNIQUE INDEX Feriado_chaveUnica ON Feriado (dataFeriado);
GO





CREATE TABLE ProdutoInventario (
  id INTEGER  NOT NULL   IDENTITY ,
  nomeProduto VARCHAR(200)  NOT NULL  ,
  categoria INT  NOT NULL  ,
  especificacao TEXT    ,
  flagEmprestavel BIT      ,
PRIMARY KEY(id));
GO




CREATE TABLE TipoAtividade (
  id INTEGER  NOT NULL   IDENTITY ,
  nome VARCHAR(64)  NOT NULL    ,
PRIMARY KEY(id)  );
GO


CREATE UNIQUE INDEX TipoAtividade_chaveUnica ON TipoAtividade (nome);
GO




CREATE TABLE FraseDia (
  id BIGINT  NOT NULL   IDENTITY ,
  Colaborador_id SMALLINT  NOT NULL  ,
  frase VARCHAR(1024)    ,
  dataRegistro DATETIME    ,
  autor VARCHAR(64)      ,
PRIMARY KEY(id)  ,
  FOREIGN KEY(Colaborador_id)
    REFERENCES Colaborador(id));
GO


CREATE INDEX FraseDia_FKIndex1 ON FraseDia (Colaborador_id);
GO


CREATE INDEX IFK_Colaborador_tem_frasesDia ON FraseDia (Colaborador_id);
GO


CREATE TABLE Cliente (
  id INTEGER  NOT NULL   IDENTITY ,
  ColaboradorGG_id SMALLINT  NOT NULL  ,
  nomeEmpresa VARCHAR(64)  NOT NULL  ,
  cnpj VARCHAR(19)  NOT NULL  ,
  endereco VARCHAR(128)    ,
  cidade VARCHAR(64)    ,
  telefone VARCHAR(15)      ,
PRIMARY KEY(id)    ,
  FOREIGN KEY(ColaboradorGG_id)
    REFERENCES Colaborador(id));
GO


CREATE UNIQUE INDEX Cliente_chaveUnica ON Cliente (cnpj);
GO
CREATE INDEX Cliente_FKIndex1 ON Cliente (ColaboradorGG_id);
GO


CREATE INDEX IFK_Cliente_tem_GG ON Cliente (ColaboradorGG_id);
GO


CREATE TABLE Equipe (
  id INTEGER  NOT NULL   IDENTITY ,
  Coordenador_id SMALLINT  NOT NULL  ,
  nome VARCHAR(25)  NOT NULL  ,
  descricao VARCHAR(128)    ,
  flagAtiva BIT    ,
  tipoEquipe SMALLINT      ,
PRIMARY KEY(id)  ,
  FOREIGN KEY(Coordenador_id)
    REFERENCES Colaborador(id));
GO


CREATE INDEX Equipe_FKIndex1 ON Equipe (Coordenador_id);
GO



CREATE INDEX IFK_Equipe_tem_Coordenador ON Equipe (Coordenador_id);
GO


CREATE TABLE Area (
  id INTEGER  NOT NULL   IDENTITY ,
  ColaboradorGF_id SMALLINT  NOT NULL  ,
  Cliente_id INTEGER  NOT NULL  ,
  nome VARCHAR(20)      ,
PRIMARY KEY(id)      ,
  FOREIGN KEY(ColaboradorGF_id)
    REFERENCES Colaborador(id),
  FOREIGN KEY(Cliente_id)
    REFERENCES Cliente(id));
GO


CREATE INDEX Area_FKIndex1 ON Area (Cliente_id);
GO
CREATE UNIQUE INDEX Area_chaveUnica ON Area (Cliente_id, nome);
GO
CREATE INDEX Area_FKIndex2 ON Area (ColaboradorGF_id);
GO


CREATE INDEX IFK_Area_tem_GF ON Area (ColaboradorGF_id);
GO
CREATE INDEX IFK_Cliente_tem_areas ON Area (Cliente_id);
GO


CREATE TABLE ItemInventario (
  id INTEGER  NOT NULL   IDENTITY ,
  ProdutoInventario_id INTEGER  NOT NULL  ,
  Colaborador_id SMALLINT    ,
  nroSerie VARCHAR(64)    ,
  nroPatrimonio VARCHAR(32)    ,
  nroNotaFiscal VARCHAR(32)    ,
  dataEmissaoNota DATETIME    ,
  dataEmprestimo DATETIME      ,
PRIMARY KEY(id, ProdutoInventario_id)    ,
  FOREIGN KEY(Colaborador_id)
    REFERENCES Colaborador(id),
  FOREIGN KEY(ProdutoInventario_id)
    REFERENCES ProdutoInventario(id));
GO


CREATE INDEX ItensInventario_FKIndex1 ON ItemInventario (Colaborador_id);
GO
CREATE INDEX ItensInventario_FKIndex2 ON ItemInventario (ProdutoInventario_id);
GO


CREATE INDEX IFK_Colaborador_tem_ItensInven ON ItemInventario (Colaborador_id);
GO
CREATE INDEX IFK_ProdutosInventario_tem_Ite ON ItemInventario (ProdutoInventario_id);
GO


CREATE TABLE MuralRecados (
  id BIGINT  NOT NULL   IDENTITY ,
  ColaboradorAutor_id SMALLINT  NOT NULL  ,
  ColaboradorDest_id SMALLINT    ,
  descricao VARCHAR(1024)    ,
  assunto VARCHAR(64)  NOT NULL  ,
  dataRegistro DATETIME  NOT NULL  ,
  flagAssuntoTrabalho BIT      ,
PRIMARY KEY(id)    ,
  FOREIGN KEY(ColaboradorAutor_id)
    REFERENCES Colaborador(id),
  FOREIGN KEY(ColaboradorDest_id)
    REFERENCES Colaborador(id));
GO


CREATE INDEX MuralRecados_FKIndex1 ON MuralRecados (ColaboradorAutor_id);
GO
CREATE INDEX MuralRecados_FKIndex2 ON MuralRecados (ColaboradorDest_id);
GO


CREATE INDEX IFK_Colaborador_escreve_recado ON MuralRecados (ColaboradorAutor_id);
GO
CREATE INDEX IFK_Colaborador_recebe_recados ON MuralRecados (ColaboradorDest_id);
GO


CREATE TABLE EquipeColaborador (
  Equipe_id INTEGER  NOT NULL  ,
  Colaborador_id SMALLINT  NOT NULL    ,
PRIMARY KEY(Equipe_id, Colaborador_id)    ,
  FOREIGN KEY(Equipe_id)
    REFERENCES Equipe(id),
  FOREIGN KEY(Colaborador_id)
    REFERENCES Colaborador(id));
GO


CREATE INDEX Equipe_has_Colaborador_FKIndex1 ON EquipeColaborador (Equipe_id);
GO
CREATE INDEX Equipe_has_Colaborador_FKIndex2 ON EquipeColaborador (Colaborador_id);
GO


CREATE INDEX IFK_Equipe_tem_colaboradores ON EquipeColaborador (Equipe_id);
GO
CREATE INDEX IFK_Colaborador_tem_equipes ON EquipeColaborador (Colaborador_id);
GO


CREATE TABLE Produto (
  id INTEGER  NOT NULL   IDENTITY ,
  ColaboradorGPD_id SMALLINT  NOT NULL  ,
  ColaboradorCoord_id SMALLINT    ,
  Area_id INTEGER  NOT NULL  ,
  nome VARCHAR(20)  NOT NULL  ,
  tempoLimiteRegistro SMALLINT      ,
PRIMARY KEY(id)        ,
  FOREIGN KEY(Area_id)
    REFERENCES Area(id),
  FOREIGN KEY(ColaboradorGPD_id)
    REFERENCES Colaborador(id),
  FOREIGN KEY(ColaboradorCoord_id)
    REFERENCES Colaborador(id));
GO


CREATE UNIQUE INDEX Produto_chaveUnica ON Produto (Area_id, nome);
GO
CREATE INDEX Produto_FKIndex1 ON Produto (Area_id);
GO
CREATE INDEX Produto_FKIndex2 ON Produto (ColaboradorGPD_id);
GO
CREATE INDEX Produto_FKIndex3 ON Produto (ColaboradorCoord_id);
GO


CREATE INDEX IFK_Area_tem_produtos ON Produto (Area_id);
GO
CREATE INDEX IFK_Produto_tem_GPD ON Produto (ColaboradorGPD_id);
GO
CREATE INDEX IFK_Produto_tem_Coordenador ON Produto (ColaboradorCoord_id);
GO


CREATE TABLE Modulo (
  id INTEGER  NOT NULL   IDENTITY ,
  Produto_id INTEGER  NOT NULL  ,
  nome VARCHAR(64)  NOT NULL  ,
  tipoLinguagem TINYINT  NOT NULL  ,
  descricao VARCHAR(512)      ,
PRIMARY KEY(id)  ,
  FOREIGN KEY(Produto_id)
    REFERENCES Produto(id));
GO


CREATE INDEX Modulo_FKIndex1 ON Modulo (Produto_id);
GO


CREATE INDEX IFK_Produto_tem_modulos ON Modulo (Produto_id);
GO


CREATE TABLE Projeto (
  id INTEGER  NOT NULL   IDENTITY ,
  ColaboradorGPJ_id SMALLINT    ,
  Produto_id INTEGER  NOT NULL  ,
  codigo VARCHAR(15)  NOT NULL  ,
  codigoCliente VARCHAR(15)  NOT NULL  ,
  codigoPacote VARCHAR(15)    ,
  nome VARCHAR(64)    ,
  estado SMALLINT    ,
  descricao VARCHAR(512)    ,
  generico BIT  NOT NULL  ,
  dataCadastro DATETIME  NOT NULL  ,
  tipoFaseProjeto SMALLINT    ,
  dataInicio DATETIME    ,
  dataEncerramento DATETIME    ,
  ColaboradorResponsavel_id SMALLINT    ,
  custoAcordado VARCHAR(128)      ,
PRIMARY KEY(id)      ,
  FOREIGN KEY(Produto_id)
    REFERENCES Produto(id),
  FOREIGN KEY(ColaboradorGPJ_id)
    REFERENCES Colaborador(id));
GO


CREATE INDEX Projeto_FKIndex1 ON Projeto (Produto_id);
GO
CREATE UNIQUE INDEX Projeto_chaveUnica ON Projeto (codigo);
GO
CREATE INDEX Projeto_FKIndex3 ON Projeto (ColaboradorGPJ_id);
GO





CREATE INDEX IFK_Produto_tem_projetos ON Projeto (Produto_id);
GO
CREATE INDEX IFK_GPJ_tem_projetos ON Projeto (ColaboradorGPJ_id);
GO


CREATE TABLE Ocorrencia (
  id BIGINT  NOT NULL   IDENTITY ,
  ColaboradorResp_id SMALLINT  NOT NULL  ,
  ColaboradorGPJ_id SMALLINT  NOT NULL  ,
  Projeto_id INTEGER  NOT NULL  ,
  Modulo_id INTEGER  NOT NULL  ,
  numOcorrencia VARCHAR(15)  NOT NULL  ,
  numBugCliente VARCHAR(15)    ,
  dataAbertura DATE  NOT NULL  ,
  titulo VARCHAR(128)  NOT NULL  ,
  descricaoResumida TEXT    ,
  comportamentoEsperado TEXT    ,
  textoAnalise TEXT    ,
  textoPropostaSolucao TEXT    ,
  baseline VARCHAR(50)    ,
  versaoModulo VARCHAR(15)    ,
  tipoSeveridade TINYINT  NOT NULL  ,
  statusOcorrencia TINYINT  NOT NULL  ,
  historicoOcorrencia TEXT    ,
  flagEnviada BOOL      ,
PRIMARY KEY(id)        ,
  FOREIGN KEY(Modulo_id)
    REFERENCES Modulo(id),
  FOREIGN KEY(Projeto_id)
    REFERENCES Projeto(id),
  FOREIGN KEY(ColaboradorGPJ_id)
    REFERENCES Colaborador(id),
  FOREIGN KEY(ColaboradorResp_id)
    REFERENCES Colaborador(id));
GO


CREATE INDEX Ocorrencia_FKIndex1 ON Ocorrencia (Modulo_id);
GO
CREATE INDEX Ocorrencia_FKIndex2 ON Ocorrencia (Projeto_id);
GO
CREATE INDEX Ocorrencia_FKIndex3 ON Ocorrencia (ColaboradorGPJ_id);
GO
CREATE INDEX Ocorrencia_FKIndex4 ON Ocorrencia (ColaboradorResp_id);
GO


CREATE INDEX IFK_Modulo_tem_ocorrencias ON Ocorrencia (Modulo_id);
GO
CREATE INDEX IFK_Projeto_tem_ocorrencias ON Ocorrencia (Projeto_id);
GO
CREATE INDEX IFK_Ocorrencia_tem_GPJ ON Ocorrencia (ColaboradorGPJ_id);
GO
CREATE INDEX IFK_Ocorrencia_tem_responsavel ON Ocorrencia (ColaboradorResp_id);
GO


CREATE TABLE GrupoAtividade (
  id INTEGER  NOT NULL   IDENTITY ,
  Projeto_id INTEGER  NOT NULL  ,
  nome VARCHAR(64)      ,
PRIMARY KEY(id)  ,
  FOREIGN KEY(Projeto_id)
    REFERENCES Projeto(id));
GO


CREATE INDEX GrupoAtividade_FKIndex1 ON GrupoAtividade (Projeto_id);
GO


CREATE INDEX IFK_Projeto_tem_gruposAtividad ON GrupoAtividade (Projeto_id);
GO


CREATE TABLE ObservacoesProjeto (
  id INTEGER  NOT NULL   IDENTITY ,
  Projeto_id INTEGER  NOT NULL  ,
  observacoes VARCHAR(1024)  NOT NULL  ,
  dataInclusao DATETIME  NOT NULL  ,
  tipo INTEGER    ,
  flagVisibilidadeRestrita TINYINT      ,
PRIMARY KEY(id)  ,
  FOREIGN KEY(Projeto_id)
    REFERENCES Projeto(id));
GO


CREATE INDEX ObservacoesProjeto_FKIndex1 ON ObservacoesProjeto (Projeto_id);
GO


CREATE INDEX IFK_Projeto_tem_observacoes ON ObservacoesProjeto (Projeto_id);
GO


CREATE TABLE Compromisso (
  id INTEGER  NOT NULL  ,
  Projeto_id INTEGER  NOT NULL  ,
  nome VARCHAR(512)  NOT NULL  ,
  dataEvento DATETIME  NOT NULL  ,
  flagConcluido BIT    ,
  dataConclusao DATETIME    ,
  flagEnvioEmail BIT    ,
  tipoRepeticao TINYINT    ,
  flagRepeticaoSegunda BIT    ,
  flagRepeticaoTerca BIT    ,
  flagRepeticaoQuarta BIT    ,
  flagRepeticaoQuinta BIT    ,
  flagRepeticaoSexta BIT    ,
  flagRepeticaoSabado BIT    ,
  flagRepeticaoDomingo BIT    ,
  dataUltimoEnvio DATETIME      ,
PRIMARY KEY(id)  ,
  FOREIGN KEY(Projeto_id)
    REFERENCES Projeto(id));
GO


CREATE INDEX Compromisso_FKIndex1 ON Compromisso (Projeto_id);
GO


CREATE INDEX IFK_Projeto_tem_compromissos ON Compromisso (Projeto_id);
GO


CREATE TABLE CustoRealProjeto (
  id INTEGER  NOT NULL   IDENTITY ,
  Projeto_id INTEGER  NOT NULL  ,
  ano INTEGER    ,
  mes INTEGER    ,
  valor VARCHAR(128)      ,
PRIMARY KEY(id)  ,
  FOREIGN KEY(Projeto_id)
    REFERENCES Projeto(id));
GO


CREATE INDEX CustoRealProjeto_FKIndex1 ON CustoRealProjeto (Projeto_id);
GO


CREATE INDEX IFK_Projeto_tem_CustoReal ON CustoRealProjeto (Projeto_id);
GO


CREATE TABLE Atividade (
  id INTEGER  NOT NULL   IDENTITY ,
  GrupoAtividade_id INTEGER  NOT NULL  ,
  TipoAtividade_id INTEGER  NOT NULL  ,
  idSequencia INTEGER    ,
  nome VARCHAR(128)  NOT NULL  ,
  descricao VARCHAR(512)    ,
  qtdeHorasPrevistas INTEGER    ,
  flagContabilizarProgresso BIT    ,
  tipoAssocOcorrencia TINYINT   DEFAULT 0   ,
PRIMARY KEY(id)    ,
  FOREIGN KEY(TipoAtividade_id)
    REFERENCES TipoAtividade(id),
  FOREIGN KEY(GrupoAtividade_id)
    REFERENCES GrupoAtividade(id));
GO


CREATE INDEX Atividade_FKIndex2 ON Atividade (TipoAtividade_id);
GO
CREATE INDEX Atividade_FKIndex2 ON Atividade (GrupoAtividade_id);
GO




CREATE INDEX IFK_Atividade_tem_Tipo ON Atividade (TipoAtividade_id);
GO
CREATE INDEX IFK_GrupoAtividade_tem_ativida ON Atividade (GrupoAtividade_id);
GO


CREATE TABLE AlocacaoAtividade (
  id INTEGER  NOT NULL   IDENTITY ,
  Colaborador_id SMALLINT  NOT NULL  ,
  Atividade_id INTEGER  NOT NULL  ,
  qtdeHorasAlocadas INTEGER    ,
  qtdeHorasRestantes FLOAT    ,
  dataInicioPrevista DATETIME    ,
  flagLiberadoRegistro BIT    ,
  custoHoraAlocacao VARCHAR(128)      ,
PRIMARY KEY(id)      ,
  FOREIGN KEY(Colaborador_id)
    REFERENCES Colaborador(id),
  FOREIGN KEY(Atividade_id)
    REFERENCES Atividade(id));
GO


CREATE INDEX AlocacaoAtividade_FKIndex1 ON AlocacaoAtividade (Atividade_id);
GO
CREATE INDEX AlocacaoAtividade_FKIndex2 ON AlocacaoAtividade (Colaborador_id);
GO
CREATE INDEX AlocacaoAtividade_chaveUnica ON AlocacaoAtividade (Colaborador_id, Atividade_id);
GO



CREATE INDEX IFK_Colaborador_tem_alocacoes ON AlocacaoAtividade (Colaborador_id);
GO
CREATE INDEX IFK_Atividade_tem_alocacoes ON AlocacaoAtividade (Atividade_id);
GO


CREATE TABLE CompromissoColaborador (
  Compromisso_id INTEGER  NOT NULL  ,
  Colaborador_id SMALLINT  NOT NULL        ,
  FOREIGN KEY(Colaborador_id)
    REFERENCES Colaborador(id),
  FOREIGN KEY(Compromisso_id)
    REFERENCES Compromisso(id));
GO


CREATE INDEX PRIMARY ON CompromissoColaborador (Compromisso_id, Colaborador_id);
GO
CREATE INDEX CompromissoColaborador_FKIndex1 ON CompromissoColaborador (Colaborador_id);
GO
CREATE INDEX CompromissoColaborador_FKIndex2 ON CompromissoColaborador (Compromisso_id);
GO


CREATE INDEX IFK_Colaborador _tem_compromis ON CompromissoColaborador (Colaborador_id);
GO
CREATE INDEX IFK_Compromisso_tem_colaborado ON CompromissoColaborador (Compromisso_id);
GO


CREATE TABLE Parametros (
  id INTEGER  NOT NULL   IDENTITY ,
  Atividade_id INTEGER    ,
  nomeEmpresa VARCHAR(128)    ,
  cnpj VARCHAR(19)    ,
  endereco VARCHAR(128)    ,
  logotipo LONGBLOB    ,
  cidade VARCHAR(64)    ,
  telefone VARCHAR(15)    ,
  primeiroDiaContabil SMALLINT      ,
PRIMARY KEY(id)  ,
  FOREIGN KEY(Atividade_id)
    REFERENCES Atividade(id));
GO


CREATE INDEX Parametros_FKIndex1 ON Parametros (Atividade_id);
GO


CREATE INDEX IFK_Atividade_tem_Parametros ON Parametros (Atividade_id);
GO


CREATE TABLE RegistroAtividade (
  id INTEGER  NOT NULL   IDENTITY ,
  Ocorrencia_id BIGINT    ,
  AlocacaoAtividade_id INTEGER  NOT NULL  ,
  dataTrabalho DATETIME  NOT NULL  ,
  qtdeHorasTrabalho FLOAT  NOT NULL  ,
  horaInicial DATETIME  NOT NULL  ,
  horaFinal DATETIME  NOT NULL  ,
  descricao VARCHAR(1024)  NOT NULL  ,
  custoHoraLancamento VARCHAR(128)      ,
PRIMARY KEY(id)    ,
  FOREIGN KEY(AlocacaoAtividade_id)
    REFERENCES AlocacaoAtividade(id),
  FOREIGN KEY(Ocorrencia_id)
    REFERENCES Ocorrencia(id));
GO


CREATE INDEX RegistroAtividade_FKIndex1 ON RegistroAtividade (AlocacaoAtividade_id);
GO
CREATE INDEX RegistroAtividade_FKIndex2 ON RegistroAtividade (Ocorrencia_id);
GO


CREATE INDEX IFK_Alocacao_tem_registros ON RegistroAtividade (AlocacaoAtividade_id);
GO
CREATE INDEX IFK_Ocorrencia_tem_registros ON RegistroAtividade (Ocorrencia_id);
GO



