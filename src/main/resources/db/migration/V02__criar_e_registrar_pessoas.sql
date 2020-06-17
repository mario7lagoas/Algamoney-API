CREATE TABLE `pessoa` (
  `codigo` bigint(20) PRIMARY KEY AUTO_INCREMENT,
  `nome` varchar(50) NOT NULL,
  `ativo` varchar(5) NOT NULL,
  `logradouro` varchar(100),
  `numero` varchar(10),
  `complemento` varchar(20),
  `bairro` varchar(20),
  `cep` varchar(10),
  `cidade` varchar(20), 
  `estado` varchar(2)  
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
insert into pessoa (nome, ativo, logradouro, numero, complemento, bairro, cep, cidade, estado) 
values ("Mario Sergio", "1", "residencia", "42", "casa", " Verde Vale", "35702557", "Sete Lagoas", "MG");
insert into pessoa (nome, ativo, logradouro, numero, complemento, bairro, cep, cidade, estado) 
values ("Higor Mario", "1", "residencia", "42", "casa", " Verde Vale", "35702557", "Sete Lagoas", "MG");
insert into pessoa (nome, ativo, logradouro, numero, complemento, bairro, cep, cidade, estado) 
values ("Henrique Sergio", "1", "residencia", "42", "casa", " Verde Vale", "35702557", "Sete Lagoas", "MG");


