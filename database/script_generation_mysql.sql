CREATE TABLE Colaborador (
  id SMALLINT UNSIGNED ZEROFILL  NOT NULL   AUTO_INCREMENT,
  nome VARCHAR(25)  NOT NULL  ,
  sobrenome VARCHAR(35)  NULL  ,
  apelido VARCHAR(15) BINARY  NOT NULL  ,
  email VARCHAR(50)  NOT NULL  ,
  emailAlternativo VARCHAR(50)  NULL  ,
  usuario VARCHAR(16)  NOT NULL  ,
  senha VARCHAR(128)  NOT NULL  ,
  tipoUsuario TINYINT UNSIGNED  NOT NULL  ,
  tipoAcesso TINYINT UNSIGNED  NOT NULL  ,
  flagAtivo BIT  NULL  ,
  dataUltLogin DATETIME  NULL  ,
  diaAniversario TINYINT UNSIGNED  NULL  ,
  mesAniversario TINYINT UNSIGNED  NULL  ,
  ramal VARCHAR(15)  NULL  ,
  telResid VARCHAR(15)  NULL  ,
  telCelular VARCHAR(15)  NULL  ,
  flagTrabSegunda BIT  NULL  ,
  flagTrabTerca BIT  NULL  ,
  flagTrabQuarta BIT  NULL  ,
  flagTrabQuinta BIT  NULL  ,
  flagTrabSexta BIT  NULL  ,
  flagTrabSabado BIT  NULL  ,
  flagTrabDomingo BIT  NULL  ,
  valorCustoHora VARCHAR(128)  NULL  ,
  valorCustoMes VARCHAR(128)  NULL  ,
  foto LONGBLOB  NULL    ,
PRIMARY KEY(id)  ,
UNIQUE INDEX Colaborador_chaveUnica(usuario));



CREATE TABLE Feriado (
  id INTEGER UNSIGNED  NOT NULL   AUTO_INCREMENT,
  dataFeriado DATETIME  NOT NULL  ,
  nome VARCHAR(24)  NULL  ,
  repetir INTEGER UNSIGNED  NULL   COMMENT '0-Não repetir;1-Semanalmente; 2-Mensalmente; 3-Anualmente'   ,
PRIMARY KEY(id)  ,
UNIQUE INDEX Feriado_chaveUnica(dataFeriado));




CREATE TABLE ProdutoInventario (
  id INTEGER UNSIGNED  NOT NULL   AUTO_INCREMENT,
  nomeProduto VARCHAR(200)  NOT NULL  ,
  categoria INT  NOT NULL  ,
  especificacao TEXT  NULL  ,
  flagEmprestavel BIT  NULL    ,
PRIMARY KEY(id));



CREATE TABLE TipoAtividade (
  id INTEGER UNSIGNED  NOT NULL   AUTO_INCREMENT,
  nome VARCHAR(64)  NOT NULL    ,
PRIMARY KEY(id)  ,
UNIQUE INDEX TipoAtividade_chaveUnica(nome));



CREATE TABLE FraseDia (
  id BIGINT  NOT NULL   AUTO_INCREMENT,
  Colaborador_id SMALLINT UNSIGNED ZEROFILL  NOT NULL  ,
  frase VARCHAR(1024)  NULL  ,
  dataRegistro DATETIME  NULL  ,
  autor VARCHAR(64)  NULL    ,
PRIMARY KEY(id)  ,
INDEX FraseDia_FKIndex1(Colaborador_id),
  FOREIGN KEY(Colaborador_id)
    REFERENCES Colaborador(id)
      ON DELETE NO ACTION
      ON UPDATE NO ACTION);



CREATE TABLE Cliente (
  id INTEGER UNSIGNED  NOT NULL   AUTO_INCREMENT,
  ColaboradorGG_id SMALLINT UNSIGNED ZEROFILL  NOT NULL  ,
  nomeEmpresa VARCHAR(64)  NOT NULL  ,
  cnpj VARCHAR(19)  NOT NULL  ,
  endereco VARCHAR(128)  NULL  ,
  cidade VARCHAR(64)  NULL  ,
  telefone VARCHAR(15)  NULL    ,
PRIMARY KEY(id)  ,
UNIQUE INDEX Cliente_chaveUnica(cnpj)  ,
INDEX Cliente_FKIndex1(ColaboradorGG_id),
  FOREIGN KEY(ColaboradorGG_id)
    REFERENCES Colaborador(id)
      ON DELETE NO ACTION
      ON UPDATE NO ACTION);



CREATE TABLE Equipe (
  id INTEGER UNSIGNED  NOT NULL   AUTO_INCREMENT,
  Coordenador_id SMALLINT UNSIGNED ZEROFILL  NOT NULL  ,
  nome VARCHAR(25)  NOT NULL  ,
  descricao VARCHAR(128)  NULL  ,
  flagAtiva BIT  NULL  ,
  tipoEquipe SMALLINT UNSIGNED  NULL   COMMENT '1-permanente, 2-temporária'   ,
PRIMARY KEY(id)  ,
INDEX Equipe_FKIndex1(Coordenador_id),
  FOREIGN KEY(Coordenador_id)
    REFERENCES Colaborador(id)
      ON DELETE NO ACTION
      ON UPDATE NO ACTION);




CREATE TABLE Area (
  id INTEGER UNSIGNED  NOT NULL   AUTO_INCREMENT,
  ColaboradorGF_id SMALLINT UNSIGNED ZEROFILL  NOT NULL  ,
  Cliente_id INTEGER UNSIGNED  NOT NULL  ,
  nome VARCHAR(20)  NULL    ,
PRIMARY KEY(id)  ,
INDEX Area_FKIndex1(Cliente_id)  ,
UNIQUE INDEX Area_chaveUnica(Cliente_id, nome)  ,
INDEX Area_FKIndex2(ColaboradorGF_id),
  FOREIGN KEY(ColaboradorGF_id)
    REFERENCES Colaborador(id)
      ON DELETE NO ACTION
      ON UPDATE NO ACTION,
  FOREIGN KEY(Cliente_id)
    REFERENCES Cliente(id)
      ON DELETE NO ACTION
      ON UPDATE NO ACTION);



CREATE TABLE ItemInventario (
  id INTEGER UNSIGNED  NOT NULL   AUTO_INCREMENT,
  ProdutoInventario_id INTEGER UNSIGNED  NOT NULL  ,
  Colaborador_id SMALLINT UNSIGNED ZEROFILL  NULL  ,
  nroSerie VARCHAR(64)  NULL  ,
  nroPatrimonio VARCHAR(32)  NULL  ,
  nroNotaFiscal VARCHAR(32)  NULL  ,
  dataEmissaoNota DATETIME  NULL  ,
  dataEmprestimo DATETIME  NULL    ,
PRIMARY KEY(id, ProdutoInventario_id)  ,
INDEX ItensInventario_FKIndex1(Colaborador_id)  ,
INDEX ItensInventario_FKIndex2(ProdutoInventario_id),
  FOREIGN KEY(Colaborador_id)
    REFERENCES Colaborador(id)
      ON DELETE NO ACTION
      ON UPDATE NO ACTION,
  FOREIGN KEY(ProdutoInventario_id)
    REFERENCES ProdutoInventario(id)
      ON DELETE NO ACTION
      ON UPDATE NO ACTION);



CREATE TABLE MuralRecados (
  id BIGINT  NOT NULL   AUTO_INCREMENT,
  ColaboradorAutor_id SMALLINT UNSIGNED ZEROFILL  NOT NULL  ,
  ColaboradorDest_id SMALLINT UNSIGNED ZEROFILL  NULL  ,
  descricao VARCHAR(1024)  NULL  ,
  assunto VARCHAR(64)  NOT NULL  ,
  dataRegistro DATETIME  NOT NULL  ,
  flagAssuntoTrabalho BIT  NULL    ,
PRIMARY KEY(id)  ,
INDEX MuralRecados_FKIndex1(ColaboradorAutor_id)  ,
INDEX MuralRecados_FKIndex2(ColaboradorDest_id),
  FOREIGN KEY(ColaboradorAutor_id)
    REFERENCES Colaborador(id)
      ON DELETE NO ACTION
      ON UPDATE NO ACTION,
  FOREIGN KEY(ColaboradorDest_id)
    REFERENCES Colaborador(id)
      ON DELETE NO ACTION
      ON UPDATE NO ACTION);



CREATE TABLE EquipeColaborador (
  Equipe_id INTEGER UNSIGNED  NOT NULL  ,
  Colaborador_id SMALLINT UNSIGNED ZEROFILL  NOT NULL    ,
PRIMARY KEY(Equipe_id, Colaborador_id)  ,
INDEX Equipe_has_Colaborador_FKIndex1(Equipe_id)  ,
INDEX Equipe_has_Colaborador_FKIndex2(Colaborador_id),
  FOREIGN KEY(Equipe_id)
    REFERENCES Equipe(id)
      ON DELETE NO ACTION
      ON UPDATE NO ACTION,
  FOREIGN KEY(Colaborador_id)
    REFERENCES Colaborador(id)
      ON DELETE NO ACTION
      ON UPDATE NO ACTION);



CREATE TABLE Produto (
  id INTEGER UNSIGNED  NOT NULL   AUTO_INCREMENT,
  ColaboradorGPD_id SMALLINT UNSIGNED ZEROFILL  NOT NULL  ,
  ColaboradorCoord_id SMALLINT UNSIGNED ZEROFILL  NULL  ,
  Area_id INTEGER UNSIGNED  NOT NULL  ,
  nome VARCHAR(20)  NOT NULL  ,
  tempoLimiteRegistro SMALLINT UNSIGNED  NULL    ,
PRIMARY KEY(id)  ,
UNIQUE INDEX Produto_chaveUnica(Area_id, nome)  ,
INDEX Produto_FKIndex1(Area_id)  ,
INDEX Produto_FKIndex2(ColaboradorGPD_id)  ,
INDEX Produto_FKIndex3(ColaboradorCoord_id),
  FOREIGN KEY(Area_id)
    REFERENCES Area(id)
      ON DELETE NO ACTION
      ON UPDATE NO ACTION,
  FOREIGN KEY(ColaboradorGPD_id)
    REFERENCES Colaborador(id)
      ON DELETE NO ACTION
      ON UPDATE NO ACTION,
  FOREIGN KEY(ColaboradorCoord_id)
    REFERENCES Colaborador(id)
      ON DELETE NO ACTION
      ON UPDATE NO ACTION);



CREATE TABLE Modulo (
  id INTEGER UNSIGNED  NOT NULL   AUTO_INCREMENT,
  Produto_id INTEGER UNSIGNED  NOT NULL  ,
  nome VARCHAR(64)  NOT NULL  ,
  tipoLinguagem TINYINT UNSIGNED  NOT NULL  ,
  descricao VARCHAR(512)  NULL    ,
PRIMARY KEY(id)  ,
INDEX Modulo_FKIndex1(Produto_id),
  FOREIGN KEY(Produto_id)
    REFERENCES Produto(id)
      ON DELETE NO ACTION
      ON UPDATE NO ACTION);



CREATE TABLE Projeto (
  id INTEGER UNSIGNED  NOT NULL   AUTO_INCREMENT,
  ColaboradorGPJ_id SMALLINT UNSIGNED ZEROFILL  NULL  ,
  Produto_id INTEGER UNSIGNED  NOT NULL  ,
  codigo VARCHAR(15)  NOT NULL   COMMENT 'Código usado internamente' ,
  codigoCliente VARCHAR(15)  NOT NULL   COMMENT 'Código usado/conhecido no cliente' ,
  codigoPacote VARCHAR(15)  NULL   COMMENT 'Código do pacote, se houver' ,
  nome VARCHAR(64)  NULL  ,
  estado SMALLINT UNSIGNED  NULL  ,
  descricao VARCHAR(512)  NULL  ,
  generico BIT  NOT NULL  ,
  dataCadastro DATETIME  NOT NULL  ,
  tipoFaseProjeto SMALLINT UNSIGNED  NULL  ,
  dataInicio DATETIME  NULL  ,
  dataEncerramento DATETIME  NULL  ,
  ColaboradorResponsavel_id SMALLINT UNSIGNED ZEROFILL  NULL  ,
  custoAcordado VARCHAR(128)  NULL    ,
PRIMARY KEY(id)  ,
INDEX Projeto_FKIndex1(Produto_id)  ,
UNIQUE INDEX Projeto_chaveUnica(codigo)  ,
INDEX Projeto_FKIndex3(ColaboradorGPJ_id),
  FOREIGN KEY(Produto_id)
    REFERENCES Produto(id)
      ON DELETE NO ACTION
      ON UPDATE NO ACTION,
  FOREIGN KEY(ColaboradorGPJ_id)
    REFERENCES Colaborador(id)
      ON DELETE NO ACTION
      ON UPDATE NO ACTION);






CREATE TABLE Ocorrencia (
  id BIGINT  NOT NULL   AUTO_INCREMENT,
  ColaboradorResp_id SMALLINT UNSIGNED ZEROFILL  NOT NULL  ,
  ColaboradorGPJ_id SMALLINT UNSIGNED ZEROFILL  NOT NULL  ,
  Projeto_id INTEGER UNSIGNED  NOT NULL  ,
  Modulo_id INTEGER UNSIGNED  NOT NULL  ,
  numOcorrencia VARCHAR(15)  NOT NULL  ,
  numBugCliente VARCHAR(15)  NULL  ,
  dataAbertura DATE  NOT NULL  ,
  titulo VARCHAR(128)  NOT NULL  ,
  descricaoResumida TEXT  NULL  ,
  comportamentoEsperado TEXT  NULL  ,
  textoAnalise TEXT  NULL  ,
  textoPropostaSolucao TEXT  NULL  ,
  baseline VARCHAR(50)  NULL  ,
  versaoModulo VARCHAR(15)  NULL  ,
  tipoSeveridade TINYINT UNSIGNED  NOT NULL  ,
  statusOcorrencia TINYINT UNSIGNED  NOT NULL  ,
  historicoOcorrencia TEXT  NULL  ,
  flagEnviada BOOL  NULL    ,
PRIMARY KEY(id)  ,
INDEX Ocorrencia_FKIndex1(Modulo_id)  ,
INDEX Ocorrencia_FKIndex2(Projeto_id)  ,
INDEX Ocorrencia_FKIndex3(ColaboradorGPJ_id)  ,
INDEX Ocorrencia_FKIndex4(ColaboradorResp_id),
  FOREIGN KEY(Modulo_id)
    REFERENCES Modulo(id)
      ON DELETE NO ACTION
      ON UPDATE NO ACTION,
  FOREIGN KEY(Projeto_id)
    REFERENCES Projeto(id)
      ON DELETE NO ACTION
      ON UPDATE NO ACTION,
  FOREIGN KEY(ColaboradorGPJ_id)
    REFERENCES Colaborador(id)
      ON DELETE NO ACTION
      ON UPDATE NO ACTION,
  FOREIGN KEY(ColaboradorResp_id)
    REFERENCES Colaborador(id)
      ON DELETE NO ACTION
      ON UPDATE NO ACTION);



CREATE TABLE GrupoAtividade (
  id INTEGER UNSIGNED  NOT NULL   AUTO_INCREMENT,
  Projeto_id INTEGER UNSIGNED  NOT NULL  ,
  nome VARCHAR(64)  NULL    ,
PRIMARY KEY(id)  ,
INDEX GrupoAtividade_FKIndex1(Projeto_id),
  FOREIGN KEY(Projeto_id)
    REFERENCES Projeto(id)
      ON DELETE NO ACTION
      ON UPDATE NO ACTION);



CREATE TABLE ObservacoesProjeto (
  id INTEGER UNSIGNED  NOT NULL   AUTO_INCREMENT,
  Projeto_id INTEGER UNSIGNED  NOT NULL  ,
  observacoes VARCHAR(1024)  NOT NULL  ,
  dataInclusao DATETIME  NOT NULL  ,
  tipo INTEGER UNSIGNED  NULL  ,
  flagVisibilidadeRestrita TINYINT UNSIGNED  NULL    ,
PRIMARY KEY(id)  ,
INDEX ObservacoesProjeto_FKIndex1(Projeto_id),
  FOREIGN KEY(Projeto_id)
    REFERENCES Projeto(id)
      ON DELETE NO ACTION
      ON UPDATE NO ACTION);



CREATE TABLE Compromisso (
  id INTEGER UNSIGNED  NOT NULL  ,
  Projeto_id INTEGER UNSIGNED  NOT NULL  ,
  nome VARCHAR(512)  NOT NULL  ,
  dataEvento DATETIME  NOT NULL  ,
  flagConcluido BIT  NULL  ,
  dataConclusao DATETIME  NULL  ,
  flagEnvioEmail BIT  NULL  ,
  tipoRepeticao TINYINT  NULL  ,
  flagRepeticaoSegunda BIT  NULL  ,
  flagRepeticaoTerca BIT  NULL  ,
  flagRepeticaoQuarta BIT  NULL  ,
  flagRepeticaoQuinta BIT  NULL  ,
  flagRepeticaoSexta BIT  NULL  ,
  flagRepeticaoSabado BIT  NULL  ,
  flagRepeticaoDomingo BIT  NULL  ,
  dataUltimoEnvio DATETIME  NULL    ,
PRIMARY KEY(id)  ,
INDEX Compromisso_FKIndex1(Projeto_id),
  FOREIGN KEY(Projeto_id)
    REFERENCES Projeto(id)
      ON DELETE NO ACTION
      ON UPDATE NO ACTION);



CREATE TABLE CustoRealProjeto (
  id INTEGER UNSIGNED  NOT NULL   AUTO_INCREMENT,
  Projeto_id INTEGER UNSIGNED  NOT NULL  ,
  ano INTEGER UNSIGNED  NULL  ,
  mes INTEGER UNSIGNED  NULL  ,
  valor VARCHAR(128)  NULL    ,
PRIMARY KEY(id)  ,
INDEX CustoRealProjeto_FKIndex1(Projeto_id),
  FOREIGN KEY(Projeto_id)
    REFERENCES Projeto(id)
      ON DELETE NO ACTION
      ON UPDATE NO ACTION);



CREATE TABLE Atividade (
  id INTEGER UNSIGNED  NOT NULL   AUTO_INCREMENT,
  GrupoAtividade_id INTEGER UNSIGNED  NOT NULL  ,
  TipoAtividade_id INTEGER UNSIGNED  NOT NULL  ,
  idSequencia INTEGER UNSIGNED  NULL   COMMENT 'para ordenar as atividades' ,
  nome VARCHAR(128)  NOT NULL  ,
  descricao VARCHAR(512)  NULL  ,
  qtdeHorasPrevistas INTEGER UNSIGNED  NULL  ,
  flagContabilizarProgresso BIT  NULL   COMMENT 'qtde de horas são contabilizadas' ,
  tipoAssocOcorrencia TINYINT UNSIGNED  NULL DEFAULT 0   ,
PRIMARY KEY(id)  ,
INDEX Atividade_FKIndex2(TipoAtividade_id)  ,
INDEX Atividade_FKIndex2(GrupoAtividade_id),
  FOREIGN KEY(TipoAtividade_id)
    REFERENCES TipoAtividade(id)
      ON DELETE NO ACTION
      ON UPDATE NO ACTION,
  FOREIGN KEY(GrupoAtividade_id)
    REFERENCES GrupoAtividade(id)
      ON DELETE NO ACTION
      ON UPDATE NO ACTION);





CREATE TABLE AlocacaoAtividade (
  id INTEGER UNSIGNED  NOT NULL   AUTO_INCREMENT,
  Colaborador_id SMALLINT UNSIGNED ZEROFILL  NOT NULL  ,
  Atividade_id INTEGER UNSIGNED  NOT NULL  ,
  qtdeHorasAlocadas INTEGER UNSIGNED  NULL  ,
  qtdeHorasRestantes FLOAT  NULL  ,
  dataInicioPrevista DATETIME  NULL  ,
  flagLiberadoRegistro BIT  NULL   COMMENT 'true=libera registro de atividades, default=false' ,
  custoHoraAlocacao VARCHAR(128)  NULL    ,
PRIMARY KEY(id)  ,
INDEX AlocacaoAtividade_FKIndex1(Atividade_id)  ,
INDEX AlocacaoAtividade_FKIndex2(Colaborador_id)  ,
INDEX AlocacaoAtividade_chaveUnica(Colaborador_id, Atividade_id),
  FOREIGN KEY(Colaborador_id)
    REFERENCES Colaborador(id)
      ON DELETE NO ACTION
      ON UPDATE NO ACTION,
  FOREIGN KEY(Atividade_id)
    REFERENCES Atividade(id)
      ON DELETE NO ACTION
      ON UPDATE NO ACTION);




CREATE TABLE CompromissoColaborador (
  Compromisso_id INTEGER UNSIGNED  NOT NULL  ,
  Colaborador_id SMALLINT UNSIGNED ZEROFILL  NOT NULL    ,
INDEX PRIMARY(Compromisso_id, Colaborador_id)  ,
INDEX CompromissoColaborador_FKIndex1(Colaborador_id)  ,
INDEX CompromissoColaborador_FKIndex2(Compromisso_id),
  FOREIGN KEY(Colaborador_id)
    REFERENCES Colaborador(id)
      ON DELETE NO ACTION
      ON UPDATE NO ACTION,
  FOREIGN KEY(Compromisso_id)
    REFERENCES Compromisso(id)
      ON DELETE NO ACTION
      ON UPDATE NO ACTION);



CREATE TABLE Parametros (
  id INTEGER UNSIGNED  NOT NULL   AUTO_INCREMENT,
  Atividade_id INTEGER UNSIGNED  NULL  ,
  nomeEmpresa VARCHAR(128)  NULL  ,
  cnpj VARCHAR(19)  NULL  ,
  endereco VARCHAR(128)  NULL  ,
  logotipo LONGBLOB  NULL  ,
  cidade VARCHAR(64)  NULL  ,
  telefone VARCHAR(15)  NULL  ,
  primeiroDiaContabil SMALLINT UNSIGNED  NULL    ,
PRIMARY KEY(id)  ,
INDEX Parametros_FKIndex1(Atividade_id),
  FOREIGN KEY(Atividade_id)
    REFERENCES Atividade(id)
      ON DELETE NO ACTION
      ON UPDATE NO ACTION);



CREATE TABLE RegistroAtividade (
  id INTEGER UNSIGNED  NOT NULL   AUTO_INCREMENT,
  Ocorrencia_id BIGINT  NULL  ,
  AlocacaoAtividade_id INTEGER UNSIGNED  NOT NULL  ,
  dataTrabalho DATETIME  NOT NULL  ,
  qtdeHorasTrabalho FLOAT  NOT NULL  ,
  horaInicial DATETIME  NOT NULL  ,
  horaFinal DATETIME  NOT NULL  ,
  descricao VARCHAR(1024)  NOT NULL  ,
  custoHoraLancamento VARCHAR(128)  NULL    ,
PRIMARY KEY(id)  ,
INDEX RegistroAtividade_FKIndex1(AlocacaoAtividade_id)  ,
INDEX RegistroAtividade_FKIndex2(Ocorrencia_id),
  FOREIGN KEY(AlocacaoAtividade_id)
    REFERENCES AlocacaoAtividade(id)
      ON DELETE NO ACTION
      ON UPDATE NO ACTION,
  FOREIGN KEY(Ocorrencia_id)
    REFERENCES Ocorrencia(id)
      ON DELETE NO ACTION
      ON UPDATE NO ACTION);




