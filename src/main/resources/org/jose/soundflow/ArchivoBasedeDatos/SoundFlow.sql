-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 20-05-2025 a las 10:45:57
-- Versión del servidor: 10.4.32-MariaDB
-- Versión de PHP: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `gestormusica`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `audio`
--

CREATE TABLE `audio` (
  `idAudio` int(11) NOT NULL,
  `titulo` varchar(100) NOT NULL,
  `artista` varchar(100) NOT NULL,
  `descripcion` text NOT NULL,
  `duracion` int(11) NOT NULL,
  `tipoAudio` enum('CANCION','PODCAST','AUDIOLIBRO','') NOT NULL,
  `idUsuario` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `audio`
--

INSERT INTO `audio` (`idAudio`, `titulo`, `artista`, `descripcion`, `duracion`, `tipoAudio`, `idUsuario`) VALUES
(13, 'podcast', 'podcast', 'podcast', 4, 'AUDIOLIBRO', 2),
(28, 'kjñalkdsjfafdafadfa', 'miguel', 'miguel	', 9, 'PODCAST', 2),
(29, 'per', 'hola', 'per	', 34, 'PODCAST', 2),
(32, 'prueba2', 'prueba2', 'prueba2', 5, 'PODCAST', 9),
(33, 'prueba6', 'prueba3', 'prueba3', 6, 'AUDIOLIBRO', 9),
(34, 'mama', 'mama', 'mama	', 3, 'CANCION', 11);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `audiolibro`
--

CREATE TABLE `audiolibro` (
  `idAudio` int(11) NOT NULL,
  `idioma` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `audiolibro`
--

INSERT INTO `audiolibro` (`idAudio`, `idioma`) VALUES
(33, 'prueba3');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `cancion`
--

CREATE TABLE `cancion` (
  `idAudio` int(11) NOT NULL,
  `genero` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `cancion`
--

INSERT INTO `cancion` (`idAudio`, `genero`) VALUES
(34, 'mama');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `listareproduccion`
--

CREATE TABLE `listareproduccion` (
  `idLista` int(11) NOT NULL,
  `nombreLista` varchar(100) NOT NULL,
  `idUsuario` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `listareproduccion`
--

INSERT INTO `listareproduccion` (`idLista`, `nombreLista`, `idUsuario`) VALUES
(1, 'miguel', 2),
(6, 'Lista1', 9),
(9, 'Lista2', 9),
(12, 'mama', 11),
(13, 'mama2', 11),
(17, 'hoejla', 2);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `podcast`
--

CREATE TABLE `podcast` (
  `idAudio` int(11) NOT NULL,
  `tematica` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `podcast`
--

INSERT INTO `podcast` (`idAudio`, `tematica`) VALUES
(13, 'podcast'),
(28, '12'),
(29, 'alke'),
(32, 'prueba2');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `relacionlistaaudio`
--

CREATE TABLE `relacionlistaaudio` (
  `idLista` int(11) NOT NULL,
  `idAudio` int(11) NOT NULL,
  `fechaAgregada` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `relacionlistaaudio`
--

INSERT INTO `relacionlistaaudio` (`idLista`, `idAudio`, `fechaAgregada`) VALUES
(1, 28, '2025-05-20'),
(1, 29, '2025-05-20'),
(1, 33, '2025-05-20'),
(17, 29, '2025-05-20'),
(17, 32, '2025-05-20');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `usuario`
--

CREATE TABLE `usuario` (
  `idUsuario` int(11) NOT NULL,
  `nombre` varchar(100) NOT NULL,
  `correo` varchar(100) NOT NULL,
  `password` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `usuario`
--

INSERT INTO `usuario` (`idUsuario`, `nombre`, `correo`, `password`) VALUES
(2, 'jose', 'jose@gmail.com', 'jose'),
(5, 'hola', 'hola@hola.com', 'hola'),
(7, 'ppaa', 'joje@gmail.com', 'jone'),
(8, '18', '18@gmail.com', '18'),
(9, 'prueba', 'prueba@gmail.com', 'prueba'),
(10, 'prueba', 'prueba@gmail.com', 'prueba'),
(11, 'mama', 'mama@mama.com', 'mama');

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `audio`
--
ALTER TABLE `audio`
  ADD PRIMARY KEY (`idAudio`);

--
-- Indices de la tabla `audiolibro`
--
ALTER TABLE `audiolibro`
  ADD PRIMARY KEY (`idAudio`);

--
-- Indices de la tabla `cancion`
--
ALTER TABLE `cancion`
  ADD PRIMARY KEY (`idAudio`);

--
-- Indices de la tabla `listareproduccion`
--
ALTER TABLE `listareproduccion`
  ADD PRIMARY KEY (`idLista`);

--
-- Indices de la tabla `podcast`
--
ALTER TABLE `podcast`
  ADD PRIMARY KEY (`idAudio`);

--
-- Indices de la tabla `relacionlistaaudio`
--
ALTER TABLE `relacionlistaaudio`
  ADD PRIMARY KEY (`idLista`,`idAudio`),
  ADD KEY `idAudio` (`idAudio`);

--
-- Indices de la tabla `usuario`
--
ALTER TABLE `usuario`
  ADD PRIMARY KEY (`idUsuario`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `audio`
--
ALTER TABLE `audio`
  MODIFY `idAudio` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=36;

--
-- AUTO_INCREMENT de la tabla `listareproduccion`
--
ALTER TABLE `listareproduccion`
  MODIFY `idLista` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=18;

--
-- AUTO_INCREMENT de la tabla `usuario`
--
ALTER TABLE `usuario`
  MODIFY `idUsuario` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=12;

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `audio`
--
ALTER TABLE `audio`
  ADD CONSTRAINT `audio_ibfk_1` FOREIGN KEY (`idUsuario`) REFERENCES `usuario` (`idUsuario`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Filtros para la tabla `audiolibro`
--
ALTER TABLE `audiolibro`
  ADD CONSTRAINT `audiolibro_ibfk_1` FOREIGN KEY (`idAudio`) REFERENCES `audio` (`idAudio`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Filtros para la tabla `cancion`
--
ALTER TABLE `cancion`
  ADD CONSTRAINT `cancion_ibfk_1` FOREIGN KEY (`idAudio`) REFERENCES `audio` (`idAudio`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Filtros para la tabla `listareproduccion`
--
ALTER TABLE `listareproduccion`
  ADD CONSTRAINT `listareproduccion_ibfk_1` FOREIGN KEY (`idUsuario`) REFERENCES `usuario` (`idUsuario`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Filtros para la tabla `podcast`
--
ALTER TABLE `podcast`
  ADD CONSTRAINT `podcast_ibfk_1` FOREIGN KEY (`idAudio`) REFERENCES `audio` (`idAudio`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Filtros para la tabla `relacionlistaaudio`
--
ALTER TABLE `relacionlistaaudio`
  ADD CONSTRAINT `relacionlistaaudio_ibfk_1` FOREIGN KEY (`idLista`) REFERENCES `listareproduccion` (`idLista`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `relacionlistaaudio_ibfk_2` FOREIGN KEY (`idAudio`) REFERENCES `audio` (`idAudio`) ON DELETE CASCADE ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
