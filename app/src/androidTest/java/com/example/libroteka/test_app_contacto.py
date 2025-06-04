from appium import webdriver
from appium.webdriver.common.appiumby import AppiumBy
from appium.options.android import UiAutomator2Options
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
import time

def wait_for_element(driver, element_id, timeout=10):
    try:
        element = WebDriverWait(driver, timeout).until(
            EC.presence_of_element_located((AppiumBy.ID, element_id))
        )
        return element
    except Exception as e:
        print(f"Error al esperar el elemento {element_id}: {str(e)}")
        raise

def test_navegacion():
    # Configurar el driver
    options = UiAutomator2Options()
    options.platform_name = "Android"
    options.device_name = "emulator-5554"
    options.platform_version = "16"
    options.automation_name = "UiAutomator2"
    options.app_package = "com.example.libroteka"
    options.app_activity = ".main_login"
    options.no_reset = True
    options.new_command_timeout = 60

    driver = webdriver.Remote("http://localhost:4723", options=options)

    try:
        time.sleep(3)

        # Click en el botón de login inicial
        btn_login = wait_for_element(driver, "com.example.libroteka:id/btn_login")
        btn_login.click()
        time.sleep(2)

        # Llenar los campos de email y contraseña
        email_field = wait_for_element(driver, "com.example.libroteka:id/et_email")
        password_field = wait_for_element(driver, "com.example.libroteka:id/et_pass")
        login_button = wait_for_element(driver, "com.example.libroteka:id/button")

        email_field.clear()
        email_field.send_keys("admin@admin.com")
        time.sleep(1)

        password_field.clear()
        password_field.send_keys("Superusuario12!")
        time.sleep(1)

        login_button.click()
        time.sleep(5)

        # Verificar actividad actual
        current_activity = driver.current_activity
        print(f"Actividad actual: {current_activity}")

        if "Home" in current_activity or "home" in current_activity:
            print("Login exitoso, pantalla principal alcanzada.")

        # Navegar al perfil
        profile_icon = wait_for_element(driver, "com.example.libroteka:id/icon_profile", timeout=15)
        profile_icon.click()
        time.sleep(4)

        # Clic en botón "Contacto"
        contacto_button = wait_for_element(driver, "com.example.libroteka:id/contactUsButton")
        contacto_button.click()
        time.sleep(2)

        # Completar formulario de contacto
        name_input = wait_for_element(driver, "com.example.libroteka:id/et_name")
        email_input = wait_for_element(driver, "com.example.libroteka:id/emailText")
        message_input = wait_for_element(driver, "com.example.libroteka:id/descriptionText")
        submit_button = wait_for_element(driver, "com.example.libroteka:id/submitButton")

        name_input.send_keys("Juan Pérez")
        email_input.send_keys("juanperez@email.com")
        message_input.send_keys("Hola, este es un mensaje de prueba desde Appium.")
        submit_button.click()

        print("Formulario enviado correctamente.")
        time.sleep(3)

    except Exception as e:
        print(f"Error durante la prueba: {str(e)}")

    finally:
        driver.quit()


# Ejecutar el test si se llama este archivo directamente
if __name__ == "__main__":
    test_navegacion()
