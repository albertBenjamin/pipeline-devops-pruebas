import org.cl.*

def call(){
	pipeline {
    agent any

    		parameters {
			choice(
				name: 'Herramienta',
				choices: ['Gradle', 'Maven'],
				description: 'Selección herramienta de construcción')
			string(
				name: 'Stage',
				defaultValue: '',
				description:
				'''Selección de stage.
				Opciones para Gradle: Build; Sonar; Run; Test; Nexus. 
				Opciones para Maven: Compile; Unit; Jar; Sonar; Nexus.''')
							}

    stages {
        stage('Pipeline') {
        	steps{
	            script{
					
					// Captura herramienta de construcción seleccionada.
					tool = params.Herramienta;
					// Inicializa env.stage global para capturar nombre de etapa.
					env.stage = '';
					// Asigna las etapas seleccionadas a variable global env.stagesString.
					env.stagesString = params.Stage.toLowerCase();
					
					env.stagesString = env.stagesString.replaceAll(~/\s/,"");
					
					if (tool == 'Gradle') {
						continuousIntegration 'runGradle';
					}
					else if (tool == 'Maven') {
						continuousIntegration 'runMaven';
					}

	            }
	        }
        }

    }
    post {
        success{
			slackSend channel: 'U01DD0BR7H8', color: 'good', message: 'Ejecución exitosa :'+['Albert Muñoz ']+[env.JOB_NAME]+[params.Herramienta], teamDomain: 'dipdevopsusach2020', tokenCredentialId: 'slack'
        }
        failure{
            slackSend channel: 'U01DD0BR7H8', color: 'danger', message: 'Ejecución fallida :'+['Albert Muñoz ']+[env.JOB_NAME]+[params.Herramienta]+' en stage' + [env.STAGE_NAME], teamDomain: 'dipdevopsusach2020', tokenCredentialId: 'slack'
        }
    }

}

}

return this;
