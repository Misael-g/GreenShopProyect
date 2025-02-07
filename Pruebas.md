📌 PRUEBAS UNITARIAS Y DE INTEGRACIÓN

📝 Introducción

Este documento contiene la evidencia de las pruebas realizadas en el sistema GreenShop. Se incluyen pruebas unitarias para métodos individuales y pruebas de integración para verificar el funcionamiento entre componentes.

✅ PRUEBAS UNITARIAS

Las pruebas unitarias evalúan la funcionalidad de métodos individuales para garantizar su correcto funcionamiento.

🔹 1. Cargar productos desde la base de datos

Descripción: Verificar que los productos se cargan correctamente en la tabla.

Escenarios:

🟢 Caso válido: Existen productos en la base de datos.

🔴 Caso límite: No hay productos en la base de datos.

Evidencia: Captura 1

🔹 2. Añadir producto al carrito

Descripción: Comprobar que los productos se añaden correctamente al carrito.

Escenarios:

🟢 Caso válido: Se añade un producto con stock disponible.

🔴 Caso límite: Se intenta añadir un producto sin stock.

Evidencia: Captura 2

🔹 3. Eliminar producto del carrito

Descripción: Verificar que los productos pueden eliminarse correctamente.

Escenarios:

🟢 Caso válido: Se elimina un producto del carrito.

🔴 Caso límite: Se intenta eliminar un producto inexistente.

Evidencia: Captura 3

🔹 4. Calcular total del carrito

Descripción: Validar que el total del carrito se calcula correctamente.

Escenarios:

🟢 Caso válido: Hay productos en el carrito.

🔴 Caso límite: El carrito está vacío.

Evidencia: Captura 4

🔗 PRUEBAS DE INTEGRACIÓN

Las pruebas de integración validan la comunicación entre diferentes módulos del sistema.

🔹 1. Compra de productos y actualización de stock

Descripción: Verificar que, al confirmar una compra, el stock se actualiza correctamente.

Escenarios:

🟢 Caso válido: Se realiza una compra con éxito.

🔴 Caso límite: Intentar comprar más productos de los que hay en stock.

Evidencia: Captura 5

🔹 2. Generación de factura en PDF

Descripción: Asegurar que, tras la compra, se genera una factura en formato PDF.

Escenarios:

🟢 Caso válido: Se genera el PDF correctamente.

🔴 Caso límite: Fallo al escribir el archivo.

Evidencia: Captura 6

🔹 3. Inicio y cierre de sesión

Descripción: Probar que los usuarios pueden iniciar y cerrar sesión correctamente.

Escenarios:

🟢 Caso válido: Credenciales correctas, el usuario inicia sesión.

🔴 Caso límite: Credenciales incorrectas, el usuario no puede acceder.

Evidencia: Captura 7
