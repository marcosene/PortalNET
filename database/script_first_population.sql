use factinet;

insert into Colaborador(nome, sobrenome, apelido, email, usuario, senha, flagAtivo,
						diaAniversario, mesAniversario, tipoUsuario, tipoAcesso, flagTrabSegunda, flagTrabTerca,
						flagTrabQuarta, flagTrabQuinta, flagTrabSexta, flagTrabSabado, flagTrabDomingo)
		values('Administrador', '', 'Administrador', 'admin@admin.com', 'admin', 
				'vhb/LPXrOTfxGlZ+1iVwzQ==', 1, 1, 1, 0, 0, 1, 1, 1, 1, 1, 1, 1);

insert into Parametros (Atividade_id, nomeEmpresa, cnpj, endereco, logotipo, cidade, 
						telefone, primeiroDiaContabil) 
			values (NULL, '', '', '', '', '', '', 1);

