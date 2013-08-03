tara.jar
==============

## Описание
Модуль для работы с хранилищами tara

## Что это и зачем ?
Модуль предназначен для упаковки/распаковки файлов ресурсов игрового сервера tankionline.com

## Как использовать ?  
Требуется java. Команда запуска  
java -jar tara.jar -f arg  [-h] [-a] [-e] [-d arg ] [-c arg ] [-v [arg] ]  
- -f, --file  
Опция обязательна, аргумент обязателен  
Файл tara  
- -h, --help  
Опция необязательна, аргумента не требуется  
Печать справки  
- -a, --add  
Опция необязательна, аргумента не требуется  
Создать хранилище  
- -e, --extract  
Опция необязательна, аргумента не требуется  
Распаковать хранилище  
- -d, --directory  
Опция необязательна, аргумент обязателен  
Рабочий каталог  
- -c, --content  
Опция необязательна, аргумент обязателен  
Файл содержимого хранилища  
- -v, --verbose  
Опция необязательна, аргумент необязателен  
Уровень вывода сообщений  

## Примеры:  
- Распаковать хранилище library.tara в текущий каталог  

> java -jar tara.jar -e -f ./library.tara  

- Распаковать хранилище library.tara в каталог content с сохранением списка содержимого в файле library.content  

> java -jar tara.jar -e -f ./library.tara -d ./content -c ./library.content  

- Создать хранилище libtary_new.tara из списка содержимого library.content с файлами, хранящимися в каталоге content  

> java -jar tara.jar -a -f ./library_new.tara -d ./content -c ./library.content  

## Облегчающие работу скрипты  
Рекурсивно обрабатывают подкаталоги  

- [Unix](https://raw.github.com/shmalevoz/tara/master/tara)  

> tara build|unpack|repair|clear-builds directory  

- unpack распаковать  
- build упаковать  
- repair восстановить из резервной копии  
- clear-builds удалить файлы промежуточных сборок  
- directory каталог проекта  

														
