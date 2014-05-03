#include<stdio.h>
#include<stdlib.h>
#include<string.h>
#include"files.h"

char xor(char a, char b){   // epistrefei to xor 2 ari8mwn binary
	char temp;
	if(a == b)
		temp = 0;
	else temp = 1;
	return temp;
}               

	
	

char* asci_to_bin(unsigned char ch){  
	char *temp;
	int i = 0 , asci;
	temp = malloc(8*sizeof(char));
	memset(temp , 0 , 8);
	asci = (int)ch;
	
	while(i<=7)
	{
		temp[i] = asci%2; 
	    asci = asci >> 1;
	    i++;
	}	
	return temp;
}								

unsigned char bin_to_asci(char *a){
	unsigned char dec = 0;
	int   i = 0;
	
	while(i<=7){
		if(i == 0)
			dec +=a[i];
		else
			dec += (a[i] << i);
		i++; 
	}
	return dec;
}




void  hex_to_bin(char ch , char* bin ){      
	int i = 0 , temp;

	switch(ch){
		case '0' : 	temp = 0;
					break;
		case '1' : 	temp = 1;
					break;
		case '2' : 	temp = 2;
					break;
		case '3' : 	temp = 3;
					break;
		case '4' : 	temp = 4;
					break;
		case '5' : 	temp = 5;
					break;
		case '6' : 	temp = 6;
					break;
		case '7' : 	temp = 7;
					break;
		case '8' : 	temp = 8;
					break;
		case '9' : 	temp = 9;
					break;
		case 'a' : 	temp = 10;
					break;
		case 'b' : 	temp = 11;
					break;
		case 'c' : 	temp = 12;
					break;
		case 'd' : 	temp = 13;
					break;
		case 'e' : 	temp = 14;
					break;
		case 'f' : 	temp = 15;
					break;
	}
	while(i<=3)
	{
		bin[i] = temp%2; 
	    	temp = temp >> 1;
	    	i++;
	}
	return;
}



char bin_to_hex(char *bin){ // prwta decimal and then hex
	char hex , dec = 0;
	int  i = 0;
	
	while(i<=3){
		if(i == 0)
			dec += bin[i];
		else
			dec += bin[i] << i;
		i++; 
	}	
	// antistoixisi tou dec me to hex
	switch(dec){
		case 0: 	hex = '0';
					break;
		case 1: 	hex = '1';
					break;
		case 2: 	hex = '2';
					break;
		case 3: 	hex = '3';
					break;	
		case 4: 	hex = '4';
					break;	
		case 5: 	hex = '5';
					break;
		case 6: 	hex = '6';
					break;						
		case 7: 	hex = '7';
					break;
		case 8: 	hex = '8';
					break;
		case 9: 	hex = '9';
					break;
		case 10: 	hex = 'a';
					break;
		case 11: 	hex = 'b';
					break;
		case 12: 	hex = 'c';
					break;					
		case 13: 	hex = 'd';
					break;
		case 14: 	hex = 'e';
					break;
		case 15: 	hex = 'f';
					break;
	}
	return hex;
}


	
