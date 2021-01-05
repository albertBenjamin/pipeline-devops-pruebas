def call(String selectStage = '') {

	switch (selectStage) {

		case 'compile':
		stage('compile') {
			println 'Compile Maven';
			env.stage = "${env.STAGE_NAME}";
			bat 'mvn clean compile -e';
		}
		break;

		case 'unit':
		stage('unit') {
			env.stage = "${env.STAGE_NAME}";
			bat 'mvn clean test -e';
		}
		break;

		case 'jar':
		stage('jar') {
			env.stage = "${env.STAGE_NAME}";
			bat 'mvn clean package -e';
		}
		break;

		case 'sonar':
		stage('sonar') {
			env.stage = "${env.STAGE_NAME}";
			script {
			def scannerHome = tool 'sonar-scanner';
		        withSonarQubeEnv('sonar') {
		            bat "${scannerHome}/bin/sonar-scanner -Dsonar.projectKey=ejemplo-gradle -Dsonar.java.binaries=build" 
		         } 
			}
		}
		break;
		case 'nexus':
		stage('nexus'){
							nexusPublisher nexusInstanceId: 'nexus', nexusRepositoryId: 'test-nexus', 
							packages: [[$class: 'MavenPackage', mavenAssetList: [[classifier: '', extension: '', 
							filePath: 'build/DevOpsUsach2020-0.0.1.jar']], 
							mavenCoordinate: [artifactId: 'DevOpsUsach2020', groupId: 'com.devopsusach2020', packaging: 'jar', version: '0.0.1']]]
						}
			
		break;

	}

}

return this;