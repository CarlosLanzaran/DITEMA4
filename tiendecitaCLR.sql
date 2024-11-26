-- Crear la base de datos
CREATE DATABASE tiendecitaCLR;

-- Usar la base de datos
USE tiendecitaCLR;

-- Crear la tabla Artículos
CREATE TABLE Articulos (
    idArticulo int auto_increment,  
    descripcion VARCHAR(50) NOT NULL,
    precio int not null,
    cantidad int NOT NULL, primary key (idArticulo) 
);

-- Crear la tabla Tickets
CREATE TABLE Tickets (
    idTicket int auto_increment, codigo varchar(45) not null, 
    fecha DATE NOT NULL,                 
    totalprecio int NOT NULL, PRIMARY KEY (idTicket)            
);

CREATE TABLE ArticulosTickets (
    idCantidadArticulo INT auto_increment,        
    idArticuloFk INT,                      
    idTicketFk INT, 
    primary key (idCantidadArticulo), foreign key (idArticuloFK) references Articulos (idArticulo),
foreign key (idTicketFK) references Tickets (idTicket)
);

select * from Articulos;
select * from Tickets;
select * from articulosTickets;

-- drop database tiendecitaCLR; --