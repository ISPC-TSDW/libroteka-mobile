from appium import webdriver
from appium.webdriver.common.appiumby import AppiumBy
from appium.options.android import UiAutomator2Options
import time

options = UiAutomator2Options()
options.platform_name = "Android"
options.platform_version = "16"  # Ajusta según la versión de tu emulador
options.device_name = "emulator-5554"  
options.app_package = "com.example.libroteka"
options.app_activity = ".MainActivity"
options.automation_name = "UiAutomator2"

driver = webdriver.Remote("http://127.0.0.1:4723", options=options)
time.sleep(5)
driver.quit()
