# 📌 PRUEBAS UNITARIAS Y DE INTEGRACIÓN

## 📝 Introducción
Este documento contiene la evidencia de las pruebas realizadas en el sistema GreenShop. Se incluyen pruebas unitarias para métodos individuales y pruebas de integración para verificar el funcionamiento entre componentes. 

---

## ✅ PRUEBAS UNITARIAS

Las pruebas unitarias evalúan la funcionalidad de métodos individuales para garantizar su correcto funcionamiento.

##  🔹 1. Cargar productos desde la base de datos

📌 Descripción: Verificar que los productos se cargan correctamente en la tabla.

🛠 Escenarios:

🟢 Caso válido: Existen productos en la base de datos.
🔴 Caso límite: No hay productos en la base de datos.
📸 Evidencia:

![image](https://github.com/user-attachments/assets/28173dc6-1425-493c-813b-6fb623b0de9e)
![image](https://github.com/user-attachments/assets/8bab5ac9-4ec5-40b7-93d3-a99f4bf14a95)

  
###🔹 2. Añadir producto al carrito

Descripción: Comprobar que los productos se añaden correctamente al carrito.

Escenarios:

🟢 Caso válido: Se añade un producto con stock disponible.

🔴 Caso límite: Se intenta añadir un producto sin stock.

![image](https://github.com/user-attachments/assets/bc3e2d5c-7139-4888-afa5-2c2cf57bca06)
![image](https://github.com/user-attachments/assets/7c95eb33-bd0a-4c05-82de-71e761c756fb)
![image](https://github.com/user-attachments/assets/f87d78a6-b461-4912-bff1-e0cf244cb7e5)


### 🔹 3. Eliminar producto del carrito

Descripción: Verificar que los productos pueden eliminarse correctamente.

Escenarios:

🟢 Caso válido: Se elimina un producto del carrito.

🔴 Caso límite: Se intenta eliminar un producto inexistente.

![image](https://github.com/user-attachments/assets/cae987d7-324e-4042-9c9c-6f3299dbbcf8)
![image](https://github.com/user-attachments/assets/90b8239f-435b-472a-bb52-ac508b9d49ee)


### 🔹 4. Calcular total del carrito y valores negativos

Descripción: Validar que el total del carrito se calcula correctamente y no se puedan insertar negativos.

Escenarios:

🟢 Caso válido: Hay productos en el carrito.

🔴 Caso límite: El carrito está vacío.
![image](https://github.com/user-attachments/assets/66c44295-f946-4366-b0c5-962d0f001a65)
![image](https://github.com/user-attachments/assets/7d09f538-6d13-4d95-bcc4-3f13a8f0f48a)
![image](https://github.com/user-attachments/assets/6e4d71c4-0fb8-45ed-a97a-e7cb9557e0b3)

## 🔗 PRUEBAS DE INTEGRACIÓN

Las pruebas de integración validan la comunicación entre diferentes módulos del sistema.

### 🔹 1. Compra de productos y actualización de stock

Descripción: Verificar que, al confirmar una compra, el stock se actualiza correctamente.

Escenarios:

🟢 Caso válido: Se realiza una compra con éxito.

🔴 Caso límite: Intentar comprar más productos de los que hay en stock.
![image](https://github.com/user-attachments/assets/56d393f5-5465-400e-9cf7-228b08b5967d)
![image](https://github.com/user-attachments/assets/015de98b-2563-471c-8b62-be092a576c68)
![image](https://github.com/user-attachments/assets/7767d89f-f777-4bfa-aeb9-0d53459fe2b0)


### 🔹 2. Generación de factura en PDF

Descripción: Asegurar que, tras la compra, se genera una factura en formato PDF.

Escenarios:

🟢 Caso válido: Se genera el PDF correctamente.

🔴 Caso límite: Fallo al escribir el archivo.
![image](https://github.com/user-attachments/assets/015de98b-2563-471c-8b62-be092a576c68)
![image](https://github.com/user-attachments/assets/6a06d5d2-96ff-49ab-a45f-ae35110586dd)

### 🔹 3. Actualización del historial de compras

Descripción: Comprobar que una compra exitosa se registra en el historial del administrador.

Escenarios:

🟢 Caso válido: La compra se muestra en el historial.

🔴 Caso límite: No se registra la compra en la base de datos.

![image](https://github.com/user-attachments/assets/3dd7f37b-acc8-442a-bc69-f951b850e599)

