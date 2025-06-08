DROP TABLE IF EXISTS `contratos`;

CREATE TABLE `contratos` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `empresa` varchar(150) COLLATE utf8mb4_unicode_ci NOT NULL,
  `fecha_fin` date DEFAULT NULL,
  `fecha_firma` date NOT NULL,
  `fecha_inicio` date NOT NULL,
  `frecuencia_de_pago` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `funciones` text COLLATE utf8mb4_unicode_ci,
  `monto` decimal(10,2) DEFAULT NULL,
  `empleado_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK2cnvii8o52u162jnl0chx1cce` (`empleado_id`),
  CONSTRAINT `FK2cnvii8o52u162jnl0chx1cce` FOREIGN KEY (`empleado_id`) REFERENCES `usuarios` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

LOCK TABLES `contratos` WRITE;

INSERT INTO `contratos` VALUES (1,'EmpresaAAA','2025-06-30','2025-06-07','2025-06-09','Quincenal','Programador',2000000.00,2);
UNLOCK TABLES;

DROP TABLE IF EXISTS `usuarios`;
CREATE TABLE `usuarios` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `email` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `nombre` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `password` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `pregunta_seguridad` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `respuesta_seguridad` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `username` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_kfsp0s1tflm1cwlj8idhqsad0` (`email`),
  UNIQUE KEY `UK_m2dvbwfge291euvmk6vkkocao` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

LOCK TABLES `usuarios` WRITE;
INSERT INTO `usuarios` VALUES (1,'admin@admin.com','Administrador de Plataforma','$2a$10$vXGZflLXz4hu7VVmRV8YheGb4QIO8FeJFyuIZed5jF8erUFUSeoNG','admin','admin','admin'),(2,'carlos@email.com','Carlos Perez','$2a$10$/78voDLqcv6dMW0EdAprweaU2G/wBozo9RlLA0lUgxtW6RO.ZEjVe','Color Favorito?','verde','Carlos123');
UNLOCK TABLES;


