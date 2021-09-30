import java.awt.Toolkit
import java.awt.datatransfer.DataFlavor
import java.awt.datatransfer.StringSelection

plugins {
    java
}

group = "com.jaspervanmerle.apmath"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.8.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}

task<JavaExec>("runAll") {
    group = "run"

    classpath = java.sourceSets["main"].runtimeClasspath
    mainClass.set("${project.group}.Runner")
}

for (i in arrayOf(
    2, 6, 11, 18, 27,
    38, 50, 65, 81, 98,
    118, 139, 162, 187, 214,
    242, 273, 305, 338, 374,
    411, 450, 491, 534, 578
)) {
    task<JavaExec>("run${i.toString().padStart(3, '0')}") {
        group = "run"

        classpath = java.sourceSets["main"].runtimeClasspath
        mainClass.set("${project.group}.Runner")

        args = listOf("$i")
    }
}

task("copySubmission") {
    group = "run"

    doLast {
        val clipboard = Toolkit.getDefaultToolkit().systemClipboard

        val bestSubmitted = clipboard.getData(DataFlavor.stringFlavor).toString()
        if (!bestSubmitted.startsWith("AP Math\n")) {
            throw Error("Copy the \"AP Math\" table on http://azspcs.com/Account/MyAccount to clipboard and run this task again")
        }

        val bestScores = mutableMapOf<Int, Int>()
        for (line in bestSubmitted.lines().drop(2)) {
            val (n, score) = line.substring(4).split("\t")
            bestScores[n.toInt()] = score.trim().toInt()
        }

        val submissions = mutableListOf<String>()
        for (file in project.rootDir.toPath().resolve("results").toFile().listFiles()!!) {
            val n = file.nameWithoutExtension.toInt()

            val lines = file.readLines()
            val score = lines.last().split(": ")[1].replace(",", "").toInt()

            if (!bestScores.containsKey(n) || score > bestScores[n]!!) {
                submissions.add(lines[1])
            }
        }

        if (submissions.isEmpty()) {
            println("Nothing to submit")
        } else {
            val completeSubmission = submissions.joinToString(";\n")
            clipboard.setContents(StringSelection(completeSubmission), null)

            val submissionSuffix = if (submissions.size == 1) "" else "s"
            println("Copied ${submissions.size} submission$submissionSuffix to clipboard")
        }
    }
}
