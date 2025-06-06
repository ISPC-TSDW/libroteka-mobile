from appium import webdriver
from selenium.webdriver.common.by import By
from appium.webdriver.common.appiumby import AppiumBy
from appium.options.android import UiAutomator2Options
from time import sleep
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
from selenium.common.exceptions import WebDriverException, TimeoutException


# Configurar el driver
options = UiAutomator2Options()
options.platform_name = "Android"
options.device_name = "emulator-5554"
options.platform_version = "16"
options.app_package = "com.example.libroteka"
options.app_activity = ".SplashActivity"
options.automation_name = "UiAutomator2"
options.no_reset = True  # Mantener el estado de la aplicación


driver = webdriver.Remote("http://127.0.0.1:4723", options=options)
wait = WebDriverWait(driver, 20)  # Aumentamos el tiempo de espera a 20 segundos


# Esperar a que termine la pantalla de splash
sleep(5)  # Espera inicial para la pantalla de splash

# Intentar encontrar el botón de registro con diferentes IDs posibles
try:
    registro_btn = wait.until(EC.presence_of_element_located((By.ID, "com.example.libroteka:id/btn_registrer")))
except:
    try:
        registro_btn = wait.until(EC.presence_of_element_located((By.ID, "com.example.libroteka:id/btn_register")))
    except:
        registro_btn = wait.until(EC.presence_of_element_located((By.ID, "com.example.libroteka:id/btnRegistrer")))

registro_btn.click()
sleep(3)  # Aumentamos el tiempo de espera

# Campos a completar
campos = {
    'etNombre': 'Juan',
    'etApellido': 'Perez',
    'etDNI': '933587421',
    'etCorreo': 'admin12@gmail.com',
    'etContrasena': '123456789'
}

# Completar formulario
for field_id, valor in campos.items():
    try:
        elemento = wait.until(EC.presence_of_element_located((By.ID, f"com.example.libroteka:id/{field_id}")))
        elemento.clear()
        sleep(1)  # Aumentamos el tiempo entre operaciones
        
        # Manejo especial para la contraseña
        if field_id == 'etContrasena':
            # Primero hacer clic en el campo
            elemento.click()
            sleep(1)
            # Limpiar el campo nuevamente después del clic
            elemento.clear()
            sleep(1)
            # Escribir la contraseña completa de una vez
            driver.execute_script('mobile: type', {'text': valor})
            sleep(1)
            # Ocultar el teclado
            driver.hide_keyboard()
        else:
            elemento.send_keys(valor)
            
        sleep(1)
    except WebDriverException as e:
        print(f"Error al escribir en el campo {field_id}: {str(e)}")
        # Intentar una vez más
        sleep(2)
        elemento.clear()
        elemento.send_keys(valor)
        sleep(1)

# Verificar que todos los campos estén llenos antes de continuar
print("\nVerificando campos antes del registro:")
for field_id in campos.keys():
    try:
        elemento = driver.find_element(By.ID, f"com.example.libroteka:id/{field_id}")
        valor = elemento.text
        print(f"Campo {field_id}: {valor}")
    except:
        print(f"No se pudo verificar el campo {field_id}")

# Hacer clic en Registrar
try:
    btn_registrar = wait.until(EC.presence_of_element_located((By.ID, "com.example.libroteka:id/btnRegistrar")))
    print("\nIntentando registrar usuario...")
    btn_registrar.click()
    sleep(10)  # Aumentamos el tiempo de espera para la respuesta del servidor
    
    # Verificar si hay mensajes de error en la interfaz
    try:
        # Buscar cualquier TextView visible
        error_elements = driver.find_elements(By.CLASS_NAME, "android.widget.TextView")
        print("\nBuscando mensajes de error en la interfaz:")
        for element in error_elements:
            try:
                text = element.text
                if text:
                    print(f"Texto encontrado: {text}")
                    if any(word in text.lower() for word in ["error", "incorrecto", "falló", "inválido", "no válido", "ya existe"]):
                        print(f"¡Mensaje de error encontrado!: {text}")
            except:
                continue
    except Exception as e:
        print(f"Error al buscar mensajes: {str(e)}")
    
    # Verificar si estamos en la misma pantalla de registro
    try:
        if driver.find_element(By.ID, "com.example.libroteka:id/btnRegistrar").is_displayed():
            print("\nLa aplicación sigue en la pantalla de registro, lo que indica un error en el proceso")
            # Verificar el estado de los campos después del intento de registro
            print("\nEstado de los campos después del intento de registro:")
            for field_id in campos.keys():
                try:
                    elemento = driver.find_element(By.ID, f"com.example.libroteka:id/{field_id}")
                    valor = elemento.text
                    print(f"Campo {field_id}: {valor}")
                except:
                    print(f"No se pudo verificar el campo {field_id}")
    except:
        print("\nNo se encontró el botón de registro, lo que sugiere que la aplicación avanzó a otra pantalla")
        
except WebDriverException as e:
    print(f"\nError al hacer clic en el botón registrar: {str(e)}")

# Esperar un poco más antes de cerrar
sleep(3)
driver.quit()
