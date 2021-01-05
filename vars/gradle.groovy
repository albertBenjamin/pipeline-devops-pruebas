def call(String selectStage = '') {

	switch (selectStage) {

		case 'build':
		stage('build') {
			env.stage = "${env.STAGE_NAME}";
			bat './gradlew clean build';
		}
		break;

		case 'sonar':
		stage('sonar') {
			env.stage = "${env.STAGE_NAME}";
			def scannerHome = tool 'sonar-scanner';
		        withSonarQubeEnv('sonar') {
		            bat "${scannerHome}/bin/sonar-scanner -Dsonar.projectKey=ejemplo-gradle -Dsonar.java.binaries=build" 
		         } 
		}
		break;

		case 'run':
		stage('run') {
			env.stage = "${env.STAGE_NAME}";
			bat 'start gradlew bootRun &';
			sleep 20
		}
		break;

		case 'test':
		stage('test') {
			env.stage = "${env.STAGE_NAME}";
			bat 'curl http://localhost:8083/rest/mscovid/estadoMundial'
		}
		break;

		case 'nexus':
			stage('nexus'){
							nexusPublisher nexusInstanceId: 'nexus', nexusRepositoryId: 'test-nexus', 
							packages: [[$class: 'MavenPackage', mavenAssetList: [[classifier: '', extension: '', 
							filePath: 'build/libs/DevOpsUsach2020-0.0.1.jar']], 
							mavenCoordinate: [artifactId: 'DevOpsUsach2020', groupId: 'com.devopsusach2020', packaging: 'jar', version: '0.0.1']]]

						}
			
		break;

	}
}
return this;