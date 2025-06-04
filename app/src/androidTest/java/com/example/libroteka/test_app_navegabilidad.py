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

        # Click en el botón de login
        print("Iniciando proceso de login...")
        btn_login = wait_for_element(driver, "com.example.libroteka:id/btn_login")
        btn_login.click()
        time.sleep(2)

        # Encontrar y llenar los campos de login
        email_field = wait_for_element(driver, "com.example.libroteka:id/et_email")
        password_field = wait_for_element(driver, "com.example.libroteka:id/et_pass")
        login_button = wait_for_element(driver, "com.example.libroteka:id/button")

        # Limpiar y llenar el email
        email_field.clear()
        email_field.send_keys("admin@admin.com")
        time.sleep(1)

        # Limpiar y llenar la contraseña
        password_field.clear()
        password_field.send_keys("Superusuario12!")
        time.sleep(1)

        # Click en el botón de login
        login_button.click()
        time.sleep(5)

        # Verificar que estamos en la pantalla principal
        current_activity = driver.current_activity
        print(f"Actividad actual: {current_activity}")

        if "Home" in current_activity or "home" in current_activity:
            print("✅ Login exitoso, pantalla principal alcanzada.")
            
            # Navegar a la pantalla de categorías
            print("Navegando a la pantalla de categorías...")
            try:
                categorias_button = wait_for_element(driver, "com.example.libroteka:id/navigation_categories", timeout=15)
                categorias_button.click()
                time.sleep(4)  # Esperar a que cargue la vista de categorías
                
                # Verificar que estamos en la pantalla de categorías
                if "Catalogo" in driver.current_activity:
                    print("✅ Navegación a categorías exitosa")
                    
                    # Navegar al perfil haciendo click en la foto de perfil
                    print("Navegando al perfil...")
                    try:
                        profile_icon = wait_for_element(driver, "com.example.libroteka:id/icon_profile", timeout=15)
                        profile_icon.click()
                        time.sleep(4)  # Esperar a que cargue la vista de perfil
                        
                        # Verificar que estamos en la pantalla de perfil
                        if "Profile" in driver.current_activity:
                            print("✅ Navegación al perfil exitosa")
                            print("Test completado: Login -> Categorías -> Perfil")
                        else:
                            print("⚠️ No se pudo confirmar la navegación al perfil")
                    except Exception as e:
                        print(f"❌ Error al navegar al perfil: {str(e)}")
                else:
                    print("⚠️ No se pudo confirmar la navegación a categorías")
            except Exception as e:
                print(f"❌ Error al navegar a categorías: {str(e)}")
        else:
            print("⚠️ Puede que el login haya fallado o no se redireccionó correctamente.")

    except Exception as e:
        print(f"❌ Error durante la ejecución del test: {str(e)}")
        raise
    finally:
        driver.quit()

if __name__ == "__main__":
    test_navegacion()
