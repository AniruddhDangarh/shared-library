package org.cloudninja

class Javadast implements Serializable {

    def script

    Javadast(script) {
        this.script = script
    }

    def cleanWorkspace() {
        script.cleanWs()
    }

    def installZap(String version) {
        script.sh """
            wget -q https://github.com/zaproxy/zaproxy/releases/download/v${version}/ZAP_${version}_Linux.tar.gz
            tar -xvf ZAP_${version}_Linux.tar.gz
            chmod +x ZAP_${version}/zap.sh
        """
    }

    def runZapScan(String zapDir, String zapPort, String scanUrl, String reportPath) {
        script.sh """
            ${zapDir}/zap.sh -cmd -port ${zapPort} \\
            -config api.disablekey=true \\
            -quickurl ${scanUrl} -quickprogress \\
            -quickout ${reportPath}
        """
    }

    def publishReport() {
        script.publishHTML(
            target: [
                allowMissing: false,
                alwaysLinkToLastBuild: true,
                keepAll: true,
                reportDir: '.',
                reportFiles: 'zap-report.html',
                reportName: 'ZAP Scan Report',
                reportTitles: 'ZAP Report'
            ]
        )
    }
}
