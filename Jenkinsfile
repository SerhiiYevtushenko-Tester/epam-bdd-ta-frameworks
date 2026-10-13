pipeline {
    agent any

    parameters {
        string(name: 'BROWSER', defaultValue: 'chrome', description: 'Target browser: chrome, firefox, edge')
        string(name: 'ENV', defaultValue: 'qa', description: 'Test environment: qa, dev')
        string(name: 'SUITE', defaultValue: 'src/test/resources/regression.xml', description: 'Path to TestNG suite XML')
    }

    stages {
        stage('Run Automated Tests') {
            steps {
                bat "mvn clean test -Dbrowser=${params.BROWSER} -Denv=${params.ENV} -DsuiteXmlFile=${params.SUITE}"
            }
        }
    }

    post {
        always {
            junit 'target/surefire-reports/TEST-*.xml'

            archiveArtifacts artifacts: 'target/screenshots/**, logs/**', allowEmptyArchive: true
        }
    }
}
