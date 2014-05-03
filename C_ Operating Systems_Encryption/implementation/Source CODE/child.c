#include <stdio.h>
#include <stdlib.h>
#include <signal.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/wait.h>
#include "files.h"
////////////////////////////////////////////////////////////////////////////////////////////////////
volatile sig_atomic_t fl = 0;

void handle_sigusr1(int signum) {
	fl = 1;
}

void handle_sigkill(){
	printf("/\\/\\ My dad has killed me !!! Child process %d \n",getpid());
}

int main(int argc , char* argv[]){
	//previous , input file , output , key , length ,N ,  mode , offset
	int previous = atoi(argv[1]) , length = atoi(argv[5]) , l , offset = atoi(argv[8]) , mode = atoi(argv[7]);
	FILE *input , *output;
	char	*k  , *key ,*message , *bin;
	int i , j , N = atoi(argv[6]);
	char *hex , *m;
	int len = 0;
	
	hex = malloc(8);
	m = malloc(9);
	message = malloc(64);
	key = malloc(128);
	input = fopen(argv[2] , "r");
	output = fopen(argv[3] , "r+");
	k = argv[4];
	
	memset(message , 0 , 64);
	memset(key , 0 , 128);
		
	signal(SIGUSR1, handle_sigusr1);
	signal(SIGKILL, handle_sigkill);
	
	/* Send signal to parent to notify that i have set my signal handler */
	kill(getppid(), SIGUSR1);
	
	while(!fl)
		pause();     //wait for a signal
	
	if(previous != -1)
		kill(previous , SIGUSR1);
	
	//i have send a signal to the previous created child
////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	fseek(input , offset , SEEK_SET);
	fseek(output , offset , SEEK_SET);
	
	while(len<length){
		fread(m , sizeof(char) , 8 , input);
		//printf("egine to diabasma apo to input\n m = %s\n" , m);
		//printf("len = %d\n" , len);
		l = 7;
		for(i = 0 ; i < 8 ; i++){
			bin = asci_to_bin(m[7-i]); 
			for(j = 8*i ; j<((8*i)+8) ; j++)
				message[j] = bin[j-8*i];
			free(bin);	
		}
		
	
		bin = malloc(8);
	
		for(i = 0 ; i <32 ; i++){
			hex_to_bin(k[31-i] , bin);
			for(j = 4*i ; j<4*i+4 ; j++)
				key[j] = bin[j-4*i];
		}
	
		if(mode == 0)		
			message = iceberg_encrypt(key , message , N);
		else if(mode == 1)
			message = iceberg_decrypt(key , message , N);
		
		
		memset(hex , 0 , 8 );
		for(i = 0 ; i<64 ; i+=8){
			for(j = i ; j<i+8 ; j++)
				bin[j-i] = message[j];   // s_buffer has the first 4 bits from the array a[]
			hex[l] = bin_to_asci(bin);
			l--;
		}				
		//prepei na to grapsoume mesa sto arxeio e3odou
		fwrite(hex , sizeof(char) , 8 , output);
		free(bin);
		len++;
	}
	
	free(hex);
	free(m);
	free(message);
	free(key);
	
	fclose(input);
	fclose(output);
////////////////////////////////////////////////////////////////////////////////////////////////////////	
	printf("The child with pid %d has finished its work..........\n", getpid() );
	return 0;	
}
