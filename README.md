## 🧍 Sobre mí

Decidí que quería un teletrabajo por motivos de conciliación. La programación suena fuerte, así que tímidamente me acerqué a este mundillo. Empecé con tutoriales gratis y algún curso de pago para las tecnologías básicas de desarrollo web (HTML, CSS y JS), y llegué a un buen curso de Programación con Java que me enseñó bastante de las bases de programación. Hice alguna web en casa que iba a usar como portafolio y, si bien es cierto que conseguí montar lo que me propuse, hubo alguna etapa difícil que me hizo dudar de mi idoneidad para este trabajo, y sobre todo concluir que no podía hacerme con el código. La sensación de desorden mental me podía.

Decidí que tenía que enfocarlo de otra forma, más rígida. Menos cursos de YouTube, y bueno, un día me compré un e-book de Java (bastante malo, la verdad, pero si no sabes nada, algo aprendes de todo) y luego otro de patrones, y alguno más ha caído… Revisé nuevamente el curso de Programación con Java. Decidí pasar de VSCode a NetBeans porque me parecía que necesitaba empezar a tocar interfaces gráficas y no solo los típicos ejercicios de código con algún método, porque ahí ya sabía que a mí me llamaba más el desarrollo de software que la web.

Luego tuve la gran suerte de encontrar la UC Abierta, al buscar algún curso gratuito de UML para entender hasta el menor detalle de mi libro de patrones con Java de Laurent Debrauwer (este sí que es harto recomendable). El tema 2 de la asignatura Ingeniería del Software I era de UML y tenía todos los apuntes en abierto… aunque, para ser sincera, he encontrado otras fuentes mejores para hacerme una idea de bases de datos. Intento complementar el avance sobre el material teórico y la práctica, pero reconozco que he tocado poco la teoría en el último mes, que es lo que llevo con este proyecto práctico.

Antes de esto hice un par de juegos siguiendo las instrucciones de cursos en YouTube. Luego mi primera app (MJL), con interfaz gráfica, base de datos (Mongo) y strings Runnables con Janino, pero básicamente ha quedado en los inicios. Me sirvió para enfrentarme a otras cuestiones del desarrollo, pelearme con el pom, conectar la base…  
Aunque *My Java Lab* no está terminada, ha sido un impulso importante, porque lo pasé mal, pero me di cuenta de que podía hacer lo que me proponía, aunque al empezar no tuviera ni idea.

Hace tiempo que me propuse empezar a trabajar pronto, así que tenía bastantes ideas en mente. La idea de una app para un centro médico y su nombre ya estaban decididos cuando le pregunté a la IA por proyectos que pudieran ayudarme a crear algo que mostrar en la búsqueda de empleo. Me habló de arquitectura hexagonal, de Spring Boot y de no sé cuántas cosas más… Ahí ya me propuse aplicar lo que pudiera a *JAVAMED* y, desde el 26 de mayo que empecé, aquí estoy: ampliando, revisando y puliendo una app que creo que me da para meses de trabajo, pero que me encanta, que me está enseñando muchísimo, y que espero me abra alguna que otra puerta.

Porque por mucho que me guste hacer esto, **también me gusta dar de comer a mis hijos**.

---

### 🔭 ¿Cómo me veo en un futuro cercano?

Me gustaría encontrar un empleo remoto, con un ambiente de compañerismo, colaboración y respeto.  
De jornada parcial, unas 30 horas semanales, porque quiero seguir dedicando tiempo a la parte teórica de este trabajo, y no descarto la obtención de algún título académico en el futuro.  
Aunque mi prioridad es ser buena profesional y productiva desde el primer momento.


## Sobre JAVAMED: Pasado, presente y futuro del proyecto

### 📖 Evolución _(en desarrollo)_

Aún no se ha documentado el origen del proyecto ni sus primeras decisiones técnicas, pero esta sección recogerá próximamente cómo surgió JAVAMED, sus primeros pasos y cómo fue tomando forma en sus primeras versiones.

### 🔵 Descripción técnica de JAVAMED

**JAVAMED** es una aplicación de escritorio desarrollada en **Java** con interfaz construida en **JavaFX**, pensada para gestionar de forma local usuarios, citas médicas y tareas administrativas comunes en centros de atención. Trabaja con una base de datos **MySQL** y mantiene una estructura clara, directa y fácil de escalar.

La autenticación se realiza con contraseñas cifradas usando **BCrypt**, que aplica _salt_ automáticamente a cada hash, evitando coincidencias y añadiendo una capa fuerte de seguridad. Las claves están separadas del perfil de usuario en una tabla específica, lo que permite tener mejor control y aislar la información sensible.

La interfaz no es fija: se **carga en función del tipo de usuario**, y cada uno ve solo lo que le toca. El propio volcado de la interfaz limita lo que se puede hacer o no, por lo que no hace falta montar un sistema de roles más allá de eso.

El código está repartido en bloques funcionales y pensados para mantener una estructura limpia: cada parte hace lo que le toca, sin mezclar lógica ni crear dependencias cruzadas entre secciones que no deberían hablarse. Se utilizan constructores y métodos dedicados para pasar referencias entre componentes, sin generar acoplamientos raros ni relaciones forzadas.

Tampoco se busca tener un exceso de clases ni lógica inflada: cada módulo tiene su lugar, y se intenta que sea fácil de seguir, de mantener y sin complicaciones innecesarias. La idea es que el código se entienda con solo leerlo y no haya que rebuscar en cinco sitios distintos para ver qué hace algo.

Para manejar errores, hay una clase central (`ErrorsHandler`) que se encarga de mostrar mensajes claros cuando algo falla, y que deja espacio para crecer si más adelante hace falta escalar esa parte.

En general, el proyecto apuesta por una lógica directa, bien separada, que funcione sin necesidad de montar capas supercomplejas. Lo importante es que cumpla su función, que se pueda extender sin romper cosas, y que quien lo lea o mantenga se sienta cómodo navegando por él.

### 🔮 Próximos pasos

#### 🧩 Lógica funcional y experiencia de usuario
- [ ] Revisar y completar los menús y submenús así como implementar las lógicas necesarias  
- [ ] Asegurar que el volcado dinámico de la interfaz según el tipo de usuario imposibilita accesos no permitidos, sin necesidad de control de roles adicional  
- [ ] Añadir atajos de teclado y dar fluidez a la navegación  
- [ ] Añadir validaciones de todo tipo y feedbacks para el usuario  

#### 🧱 Organización del código y arquitectura
- [ ] Mantener y pulir el desacoplamiento entre capas y entre clases  
- [ ] Revisar la posibilidad de que el esquema de la base de datos pueda ser interpretado activamente desde `DBManager` para influir en cómo se formulan las consultas CRUD, o reforzar la línea actual basada en SQL definidos manualmente  
- [ ] Dar más poderío a `ErrorsHandler` y observar si con el tiempo requiere clases auxiliares  

#### 🧪 Calidad del software y validación
- [ ] Implementación real de test unitarios y pruebas que puedan ser útiles y suponer una mejoría real del código  

#### 📄 Documentación técnica
- [ ] Desarrollar una documentación adecuada para la aplicación  
