**Build**<br>
`gradlew build`<br><br>
**Startup**<br>
В директории `build/libs/` будет лежать файл `PeacockTestTask-1.1.0.jar`<br>
Из корня можно запустить командой `java -jar -Xmx1G build/libs/PeacockTestTask-1.1.0.jar `<br>
После выполнения программы вы увидете время выполнения программы в миллисекундах<br>
Файл `output.txt` будет находиться в директории тестируемого файла<br>

**Tested systems**<br>
- 1 тест:<br>
i3-8100 8 секунд<br>
i7 11700f - 4 секунды<br>
- 2 тест:<br>
i3-8100 - 18 секунд<br>
i7 11700f - не проводился<br>
