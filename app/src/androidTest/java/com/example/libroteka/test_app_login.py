from appium import webdriver
from appium.webdriver.common.appiumby import AppiumBy
from appium.options.android import UiAutomator2Options
import time

# Configurar el driver
options = UiAutomator2Options()
options.platform_name = "Android"
options.device_name = "emulator-5554"
options.platform_version = "16"
options.automation_name = "UiAutomator2"
options.app_package = "com.example.libroteka"
options.app_activity = ".main_login"
options.no_reset = True  # Mantener el estado de la app entre pruebas
options.new_command_timeout = 60  # Aumentar el timeout para comandos

# Configurar el driver con la URL correcta
driver = webdriver.Remote("http://127.0.0.1:4723", options=options)

try:
    time.sleep(3)

    
    btn_login = driver.find_element(AppiumBy.ID, "com.example.libroteka:id/btn_login")
    btn_login.click()

    time.sleep(2)

   
    email_field = driver.find_element(AppiumBy.ID, "com.example.libroteka:id/et_email")
    password_field = driver.find_element(AppiumBy.ID, "com.example.libroteka:id/et_pass")
    login_button = driver.find_element(AppiumBy.ID, "com.example.libroteka:id/button")

   
    email_field.clear()
    email_field.send_keys("admin@admin.com")

    time.sleep(1)

    
    password_field.clear()
    password_field.send_keys("Superusuario12!")

    time.sleep(1)

    
    login_button.click()

    
    time.sleep(5)
    current_activity = driver.current_activity
    print(f"Actividad actual: {current_activity}")

    if "Home" in current_activity or "home" in current_activity:
        print("✅ Login exitoso, pantalla principal alcanzada.")
    else:
        print("⚠️ Puede que el login haya fallado o no se redireccionó correctamente.")

finally:
    driver.quit()
