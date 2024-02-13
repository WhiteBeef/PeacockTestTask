**Build**<br>
`gradlew build`<br><br>
**Startup**<br>
В директории `build/libs/` будет лежать файл `PeacockTestTask-1.1.0.jar`<br>
Из корня можно запустить командой `java -jar -Xmx1200M build/libs/PeacockTestTask-1.1.0.jar `<br>
После выполнения программы вы увидете время выполнения программы в миллисекундах<br>
Файл `output.txt` будет находиться в директории тестируемого файла<br>

**Tested systems**<br>
- 1 тест:<br>
i3-8100 8-9 секунд<br>
i7 11700f - 4 секунды<br>
- 2 тест:<br>
i3-8100 - 20 секунд<br>
i7 11700f - не проводился<br>

**Примечание**<br>
У меня есть 2 решения, одинаковые по смыслу, но по-разному написаные: без использования класса Group и с его использованием.<br>
Логика та же, но в одном классе и больше в стиле олимпиадного программирования <br>
По умолчанию стоит именно оно <br>
Чтобы сбилдить другое, надо в файле build.gradle изменить строку `'Main-Class': 'ru.whitebeef.SolutionWithoutClassGroup'` на `'Main-Class': 'ru.whitebeef.SolutionWithClassGroup'`<br>
