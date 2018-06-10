var categorias = [
	"Ensaladas", "Raciones",
	"Carnes", "Pescados",
	"Postres", "Bebidas"
];

var platos = [
	[
		"Ensalada variada", 
		"Ensalada de queso de cabra a la plancha",
		"Tomate al orégano con atún"
	],
	[
		"Patatas bravas", "Patatas alioli",
		"Ensaladilla rusa", "Tortilla de patata (2 personas)",
		"Tortilla de patata grande", "Huevos rotos con picadillo",
		"Champiñón frito al ajillo", "Pimientos de padrón",
		"Croquetas de jamón", "Alitas de pollo marinadas y fritas",
		"Lacón a la gallega", "Oreja adobada a la plancha",
		"Callos a la madrileña", "Magro ibérico estofado con boletus con setas",
		"Caracoles a la madrileña", "Chorizo de León frito",
		"Mejillones al vapor", "Mejillones tigre (6 unidades)",
		"Mejillones en salsa picante de tomate", "Gambas al ajillo",
		"Calamares", "Pulpo a la gallega"
	],
	[
		"Entrecot de buey", "Solomillo de cerdo a la pimienta o con cabrales",
		"Secreto ibérico con salsa de brevas", "Morcillo de ternera blanca estofado con sidra"
	],
	[
		"Dorada o lubina a la bilbaína", "Emperador ajo-aceite"
	],
	[
		"Flan de queso-crema al caramelo", "Arroz con leche a la canela",
		"Tarta de bizcocho de manzana", "Copa de yogur natural con mermelada casera"
	],
	[
		"Fanta naranja", "Fanta limón", "Coca-cola", "Agua sin gas",
		"Mosto", "Tercio de barril", "Cerveza sin", "Vol Damm",
		"Tercio botella", "Jarra 1/2 litro cerveza", "Jarra litro cerveza",
		"Vino de la casa", "Copa Ribera del Duero", "Copa Rioja",
		"Copa de Rueda", "Copa de Rioja crianza", "Vaso sangría",
		"Jarra litro sangría"
	]
];

var precios = [
	[ 5.40, 6.60, 5.25, 3.70 ],
	[ 3.75, 3.85, 4.60, 4.86, 10.00, 6.80, 5.10, 5.50, 6.75, 6.85, 6.80,
		4.85, 6.75, 6.90, 6.75, 6.00, 5.20, 6.90, 5.90, 5.80, 6.80, 11.70
	],
	[ 12.30, 9.50, 12.00, 9.60 ],
	[ 9.00, 9.50 ],
	[ 2.80, 2.85, 2.85, 2.80 ],
	[ 2.40, 2.40, 2.40, 1.65, 1.70, 2.40, 2.40, 3.30, 2.75, 3.90, 7.30, 
		1.45, 2.25, 1.80, 1.80, 2.25, 2.40, 7.30
	]
];

var cargarBD = function() {
	db.categorias.drop();
	db.platos.drop();
	db.mesas.drop();
	for (var i=0; i<categorias.length; i++) {
		var categoria = {
			nombre 	: categorias[i]
		};
		db.Categoria.insert(categoria);
		var idCategoria=db.Categoria.find({ nombre : categoria.nombre })[0]._id;

		print("Categoría: " + categoria.nombre);
		var platosDeCategoria = platos[i];
		var preciosDeCategoria = precios[i];
		print("\t" + platosDeCategoria.length + " platos");
		print("\t" + preciosDeCategoria.length + " precios");
		for (var j=0; j<platosDeCategoria.length; j++) {
			var plato = {
				idCategoria : idCategoria,
				nombre 		: platosDeCategoria[j],
				precio 		: preciosDeCategoria[j]
			};
			db.Plato.insert(plato);
			print("\t\t" + plato.nombre + "\t\t\t\t" + plato.precio + " €");
		}
	}
	for (var i=1; i<=10; i++) {
		var mesa = {
			_id : i,
			estado : "Libre"
		};
		db.Mesa.insert(mesa);
	}
};

cargarBD();







