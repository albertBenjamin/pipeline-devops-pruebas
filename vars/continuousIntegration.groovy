def call(String run = '') {

// Transforma lista de stages ingresadas (env.stagesString) en array.
String[] stagesList = env.stagesString.split(';');
// True si stageList contiene strings vacíos.
stagesListEmpty = stagesList.any {x -> x == ''};

switch(run) {
  case 'runGradle':
    // Validación y ejecución de stages Gradle.
   	 if (stagesList.contains('build')
        || stagesList.contains('sonar')
        || stagesList.contains('run')
        || stagesList.contains('test')
        || stagesList.contains('nexus')) {
        println 'Stages ingresadas válidas' + ' '+ stagesList.join(',');
        // Ejecuta stages ingresadas.
        for (String values : stagesList) {
            println 'Ejecutando etapa ' + values ;
            gradle values;
        }

    // Validación de input de stages vacío o con espacios en blanco, para ejecutar todas las stages.
    } else if (!stagesList || stagesListEmpty) {
        println 'Stages en blanco';
        println 'Selección por defecto, ejecutando todas las etapas Gradle';
        gradle 'build';
        gradle 'sonar';
        gradle 'run';
        gradle 'test';
        gradle 'nexus';
// Envía error a la consola en caso de no cumplir las condiciones.
// Se almacena la información en env.stage para enviar a mensaje Slack.
    } else {
        env.stage = env.stagesString;
        throw new Exception('Stages no ingresadas correctamente para Gradle');
    };
    break;

  case 'runMaven':
// Determinar si las stages seleccionadas para Maven son válidas.
 if (stagesList.contains('compile')
    || stagesList.contains('unit')
    || stagesList.contains('jar')
		|| stagesList.contains('sonar')
		|| stagesList.contains('nexus')) {
    println 'Stages ingresadas válidas';
    // Ejecuta stages ingresadas.
    for (String values : stagesList) {
      println 'Ejecutando etapa ' + values;
      maven values;
    }
// Validación de input de stages vacío o con espacios en blanco, para ejecutar todas las stages.
  } else if (!stagesList || stagesListEmpty) {
      println 'Sin información ingresada.';
      println 'Selección por defecto, ejecutando todas las etapas Maven';
      maven 'compile';
      maven 'unit';
      maven 'jar';
      maven 'sonar';
      maven 'nexus';
// Envía error a la consola en caso de no cumplir las condiciones.
// Se almacena la información en env.stage para enviar mensaje a Slack.
  } else {
      env.stage = stagesString;
      throw new Exception('Stages no ingresadas correctamente para Maven');
    }

  }
}
