# Proyecto Batcher de Programación de servicios e procesos
Programa que simula un gestor batcher de forma simple. Ingiere 'Jobs' definidos en ficheros YAML, procesandolos a través del método FCFS(First Come, First Service).
#
Este simulador es capaz de hacer...
  -Ingerir ficheros YAML para procesar su información, validando los parámetros de los ficheros antes procesarlos.
  -Convertir los GB a MB del parametro que almacena la memoria para su uso.
  -Simulación de un ordenador con un número de CPUs y una cantidad de memoria de 4cores y 2048MB respectivamente.
  -El programa clasifica los jobs dependiendo de los recursos simulados del ordenados, clasificándolos en diferentes estados(NEW,WAITING,READY,RUNNING,DONE,FAILED).
  -Procesa los jobs de estado READY->RUNNING usando el método FCFS y a través de un 'ProcessBuilder'.
  -Simula un trabajo con un trabajo simple de conversion e caracteres en el tiempo indicado en el job.
  -Indica el progreso del job a través de un contador long 'progress' dividiendo la duración del programa.
  -Si el proceso acaba de manera fallida termina en el estado FAILED sin bloquear el resto de jobs.
  -Cuando el procesa finaliza libera los recursos tanto en estado DONE como en FAILED.
  -Si hay jobs pendientes en estado WAITING y los recursos disponibles los ejecutara.
  -El programa visualiza el estado de los jobs en un método que se muestra tras cada cambio de estado del proceso.
#
(Este programa no ejecuta ninguna característica adicional y no ejecuta el sistema Round Robin para los cores de la CPU)
