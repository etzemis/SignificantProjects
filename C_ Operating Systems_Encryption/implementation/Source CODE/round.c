#include<stdio.h>
#include<stdlib.h>
#include"files.h"

char* gama(char *a){            
	char *s_buffer  , *p_buffer , hex;     // buffers we use in order to call tha s-boxes and the p-boxes
	int i=0 , j=0;
	s_buffer = malloc(4*sizeof(char));
	p_buffer = malloc(8*sizeof(char));
	//klhsh tou prwtou sbox
	for(i = 0 ; i<64 ; i+=4){
		for(j = i ; j<i+4 ; j++)
			s_buffer[j-i] = a[j];   // s_buffer has the first 4 bits from the array a[]
		hex = bin_to_hex(s_buffer);
		hex = S0(hex);				//egine to s0
		hex_to_bin(hex , s_buffer);  //metatroph pali se binary
		for(j = i ; j<i+4 ; j++)    
			a[j] = s_buffer[j-i];
	}   
	//klhsh tou P8
	for(i = 0 ; i<64 ; i+=8){
		for(j = i ; j<i+8 ; j++)
			p_buffer[j-i] = a[j];   
		P8(p_buffer);
		for(j = i ; j<i+8 ; j++)    
			a[j] = p_buffer[j-i];
	}
	//klhsh tou S1
	for(i = 0 ; i<64 ; i+=4){
		for(j = i ; j<i+4 ; j++)
			s_buffer[j-i] = a[j];   // s_buffer has the first 4 bits from the array a[]
		hex = bin_to_hex(s_buffer);
		hex = S1(hex);				//egine to s0
		hex_to_bin(hex , s_buffer);  //metatroph pali se binary
		for(j = i ; j<i+4 ; j++)    
			a[j] = s_buffer[j-i];
	}   
	//klhsh tou P8
	for(i = 0 ; i<64 ; i+=8){
		for(j = i ; j<i+8 ; j++)
			p_buffer[j-i] = a[j];   
		P8(p_buffer);  //egine to P8
		for(j = i ; j<i+8 ; j++)    
			a[j] = p_buffer[j-i];
	}
	//klhsh tou S0
	for(i = 0 ; i<64 ; i+=4){
		for(j = i ; j<i+4 ; j++)
			s_buffer[j-i] = a[j];   // s_buffer has the first 4 bits from the array a[]
		hex = bin_to_hex(s_buffer);
		hex = S0(hex);				//egine to s0
		hex_to_bin(hex , s_buffer);  //metatroph pali se binary
		for(j = i ; j<i+4 ; j++)    
			a[j] = s_buffer[j-i];
	}
	//eginan oles oi apaitoumenes metatropes
	free(s_buffer);
	free(p_buffer);
	return a;
}


char* SK(char *a , char *b){    
	char xor[64];
	int i=0;
	memset(xor , 0 , 64);
	for(i = 0 ; i<64 ; i++){
		if(a[i] == b[i])
			xor[i] = 0;
		else
			xor[i] = 1;
	}
	for(i=0 ; i<64 ; i++)
		a[i] = xor[i];
	return a;
}
// epistrefei enan pinaka pou einai o xor twn eisodwn tis 
		
	
	
char* EK(char *a,char *b){                  
	char  hex ,*s_buffer , *p_buffer ;
	int i , j;
	s_buffer = malloc(4*sizeof(char));
	p_buffer = malloc(4*sizeof(char));
	//klhsh tou  P64
	P64(a);
	//klhsh tou D
	for(i = 0 ; i<64 ; i+=4){
		for(j = i ; j<i+4 ; j++)
			s_buffer[j-i] = a[j];   // s_buffer has the first 4 bits from the array a[]
		hex = bin_to_hex(s_buffer);
		hex = D(hex);				//egine to s0
		hex_to_bin(hex , s_buffer);  //metatroph pali se binary
		for(j = i ; j<i+4 ; j++)    
			a[j] = s_buffer[j-i];
	}   
	//bitwise xor  tou a[] kai tou b[]
	a = SK(a , b);   
	//klhsh tou P4 gia to P4
	for(i = 0 ; i<64 ; i+=4){
		for(j = i ; j<i+4 ; j++)
			p_buffer[j-i] = a[j];   
		P4(p_buffer);  //egine to P4
		for(j = i ; j<i+4 ; j++)    
			a[j] = p_buffer[j-i];
	}
	//klhsh tou  P64
	P64(a);
	free(s_buffer);
	free(p_buffer);	
	return a;
}
