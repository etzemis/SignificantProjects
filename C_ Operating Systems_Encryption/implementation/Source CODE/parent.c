#include <stdio.h>
#include <stdlib.h>
#include <signal.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/wait.h>
#include <string.h>
#include"files.h"

//prompt> iceberg [-e/-d] -i input file -o output file -c configuration file

volatile sig_atomic_t fl_s = 0;

void handle_sigusr1(int signum) {
	fl_s = 1;
}


int main(int argc , char* argv[]){
		int pid , N , workers = 0 , mode = 0;   // mode == 0 =>encrypt    if mode == 1 =>decrypt
		int i , j , input , output , configuration , offset = 0;
		FILE  *in , *out , *config;
		int previous = -1  , *child_pid;      // previous filaw tou pid tou proigoumenou paidiou  , child_pid = pinakas me ta pid olwn twn paidiwn
		char *buffer , *in_buff , *out_buff , *offset_buff , *length_buff , *key_buff , *N_buff , *mode_buff;
		int status , done , child , length = 0;
		long temporary;
		
		N_buff = malloc(5);
		in_buff = malloc(20);
		out_buff = malloc(20);
		offset_buff = malloc(5*sizeof(int));
		length_buff = malloc(5*sizeof(int));
		key_buff = malloc(40*sizeof(char));
		mode_buff = malloc(4);
		buffer = malloc(70);
		
		for(i = 1; i<argc ; i++){                                  // diabasma twn patametrwn eisodou
			if(strcmp(argv[i] , "-d") == 0)
				mode = 1;
			if(strcmp(argv[i] , "-e") == 0)
				mode = 0;
			if(strcmp(argv[i] , "-i") == 0)
			{
				input = i+1;
				if ((in = fopen(argv[i+1], "r")) == NULL) { 
					perror("fopen input-file");
					return 1;
				}
			}
			if(strcmp(argv[i] , "-o") == 0 )
			{
				output = i+1;
				if ((out = fopen(argv[i+1], "w+")) == NULL) { 
					perror("fopen output-file");
					return 1;
				}
			}
			if(strcmp(argv[i] , "-c") == 0)
			{
				configuration = i+1;
				if ((config = fopen(argv[i+1], "r")) == NULL) { 
					perror("fopen configuration-file");
					return 1;
				}
			}
		} 
		fscanf(config , "%d\n" , &N);
		temporary = ftell(config);
		while (feof(config) == 0){                        // diabasma twn grammwn pou exei to configuration file
			fgets(buffer,70,config);
			workers++;
		}
		workers--; // tous meiwnw giati logw tou feof diabazei 2 fores tin teleytaia grammi
		child_pid = malloc(workers*sizeof(int)); // pinakas pou 8a exei ola ta pid twn pidiwn
		/////////8a ginei gemisma tou arxeiou out me 0
		fseek(in , 0 , SEEK_END);
		offset = ftell(in);
		offset_buff = malloc(offset);
		memset(offset_buff , 0 , offset);
		fwrite(offset_buff , 1 , offset , out);
		fseek(out , 0 , SEEK_SET);
		free(offset_buff); 
//////////////////////////////////////////////////////////////
		fseek(config , temporary , SEEK_SET); // 3anapame ekei pou eimastan sto configuration file
//////////////////////////////////////////////////////////////		
		getchar();
		signal(SIGUSR1, handle_sigusr1);////orizoume oti mporei na akousei to minuma pou 8a dextei
		offset_buff = malloc(10);
		offset = 0;
		for(i = 0 ; i<workers ;i++){
			fl_s = 0;
			fscanf(config , "%s " , key_buff);
			fscanf(config , "%s\n" , length_buff);	
			
			if((child_pid[i] = pid = fork()) == -1) {
				perror("fork");
				exit(1);
			}
				
			
			/*child*/
			if (pid == 0) {
				//offset, length , input , output , key 
				sprintf(buffer , "%d" , previous);
				sprintf(in_buff , "%s" , argv[input]);
				sprintf(out_buff , "%s" , argv[output]);
				sprintf(N_buff , "%d" , N);
				sprintf(mode_buff , "%d" , mode);
				sprintf(offset_buff, "%d" , offset);				
				//printf("++++++++++++++++++++input = %s \n output = %s \n key = %s \n length = %s\n " , in_buff , out_buff , key_buff , length_buff);
				if(execlp( "./child","child" ,buffer , in_buff , out_buff , key_buff , length_buff , N_buff , mode_buff , offset_buff ,  (char *) NULL ) == -1){
				perror("exec  failure!!!");
				}
			}
			offset += 8*atoi(length_buff);
			while(!fl_s)
				pause(); /* Wait For a Signal */
			previous = pid;
		}
/////////////////////////////////////////////////////			
		
		// signal sigusr1 sto teleytaio pou dhmiourghse
		kill(child_pid[workers-1], SIGUSR1);
		
		i = 0;
		while(i<workers){
			done = 0;
			pid = wait(&status);
			for(j = 0 ; j<workers ; j++){
				if(child_pid[j] == pid){
					done = 1;
					child = j;
				}
			}
			if(done == 0){
				perror("wait failure...");
				exit(1);
			}
			if((status & 0377) == 0){ 
				printf("+++ Parent: Child Exited Normally: %d\n", status >> 8);
				child_pid[child] = -1;
			}
			else{ 
				printf("+++ Parent: Child with pid = %d  Exited due to signal: %d\n", pid , status & 0177);
				printf("I will stop the execution of all the remaining children.....");
				for(j = 0 ; j<workers ; j++){
					if(child_pid[j] != -1 && child_pid[j] != pid)
						kill(child_pid[j], SIGKILL);
				}
				remove(argv[output]);
			}
			i++;
		}	
		free(N_buff);
		free(in_buff);
		free(out_buff);
		free(offset_buff);
		free(length_buff);
		free(key_buff);
		free(mode_buff);
		free(buffer);
		
		fclose(in);
		fclose(out);
		fclose(config);
		return 0 ;
}
