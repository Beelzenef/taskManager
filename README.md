# taskManager

Gestor de tareas con API REST en Laravel.

Se gestionan tareas que contienen los siguientes campos, todos ellos obligatorios: nombre, descripción, imagen, enlace, fecha. Se pueden añadir, eliminar, modificar y también listar, que es la acción principal que desemboca en todas las demás.

La aplicación tiene usos en la vida real, aunque quizás de forma más sencilla. Puede haber tareas que no necesiten una imagen, o alguna que no necesite un enlace, o puede que las dos combinadas. Posibles ampliaciones podrían permitir elegir añadir enlace y/o imagen. El resto de campos son coherentes y coincidentes en la mayor parte de un gestor de tareas.

Puedes encontrar la API en [este enlace](http://elena.alumno.mobi/api/tasks).

### Dudas y consultas en la red

Los enlaces listados corresponden a errores que he podido resolver en la red, así como las soluciones más claras con explicaciones más directas.

* [_Specified key was too long error_](https://laravel-news.com/laravel-5-4-key-too-long-error)
* [_getting mberror error while updating Composer_](https://stackoverflow.com/a/36979121/6659852)
* [¿Dónde está AppServiceProvider?](https://github.com/laravel/laravel/blob/master/app/Providers/AppServiceProvider.php)
* [_How to reload nginx_](https://stackoverflow.com/questions/21292533/reload-nginx-configuration)
* [Migración de Laravel, _database_ no existe](https://stackoverflow.com/questions/22750995/laravel-migration-says-unknown-database-but-it-is-created)