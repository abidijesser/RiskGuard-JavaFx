create database crud;
use crud;

create table reclamations(
id_reclamation int auto_increment primary key,
nom_client varchar(20) not null,
email_client varchar(20) not null ,
description varchar(80) not null ,
num_tel int not null
)

insert into reclamations(nom_client, email_client, description, num_tel) values("Ghassen","ghassen.achour@esprit.tn","test","+21628124816");
