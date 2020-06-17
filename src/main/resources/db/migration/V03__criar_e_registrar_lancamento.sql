CREATE TABLE `lancamento` (
  `codigo` bigint(20) PRIMARY KEY AUTO_INCREMENT,
  `descricao` varchar(50) NOT NULL,
  `data_vencimento` DATE NOT NULL,
  `data_pagamento` DATE,
  `valor` DECIMAL(10,2) NOT NULL,
  `observacao` varchar(100),
  `tipo` varchar(20),
  `codigo_categoria` bigint(20) NOT NULL,
  `codigo_pessoa` bigint(20) NOT NULL, 
  FOREIGN KEY (codigo_categoria) REFERENCES categoria(codigo),
  FOREIGN KEY (codigo_pessoa) REFERENCES pessoa(codigo)
  ) ENGINE=InnoDB DEFAULT CHARSET=latin1;

insert into lancamento (descricao, data_vencimento, valor, tipo, codigo_categoria, codigo_pessoa) values ("Pagamento de fatura", '2018-07-13', 50.20, "DESPESA", 3, 2);
