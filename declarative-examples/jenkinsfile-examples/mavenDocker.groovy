//Declarative Pipeline
pipeline {
    agent any
    
    stages {
        stage('Cloning Sources') {
            steps {
                git url: 'https://github.com/fclaudiopalmeira/dotnetcore-helloworld'
            }
        }
        stage('Restoring') {
            steps {
                sh 'dotnet restore'
            }
        }
        stage('Build') {
            steps {
                sh 'dotnet build'
            }
        }
        stage('Test') {
            steps {
                sh """
                cd "$WORKSPACE"/UnitTests
                dotnet test
                """
            }
        }
    }
    post {
        success {
            emailext(
                subject: "${env.JOB_NAME} on build [${env.BUILD_NUMBER}] has been succesfully deployed.",
                body: "Please check the console output for ${env.JOB_NAME} on [${env.BUILD_URL}] ",
                to: "fclaudiopalmeira@gmail.com"
            )
        } 
        failure{
            emailext(
                subject: "${env.JOB_NAME} on build [${env.BUILD_NUMBER}] has failed.",
                body: "Please check the console output for ${env.JOB_NAME} on [${env.BUILD_URL}] ",
                to: "fclaudiopalmeira@gmail.com"
            )
        }  
    }
    stages {
        stage('Build and Publish') {
            when {
                branch 'master' //Avoiding other branches
            }
            steps {
             sh 'dotnet publish ~/published'   
            }
        }
    }
}
