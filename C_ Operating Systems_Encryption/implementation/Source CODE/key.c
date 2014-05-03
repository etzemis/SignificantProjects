#include<stdio.h>
#include"files.h"
#include<stdlib.h>

 
char* TC(char *a , int C)   
{
char temp[128];
int i;
memset(temp , 0 , 128);
for(i = 0; i<128 ; i++){
	if(C==1) temp[i] = a[(i+8)%128];
	if(C==0) temp[i] = a[(128+i-8)%128];
}
for(i = 0 ; i < 128 ; i++)
	a[i] = temp[i];
return a;
}

char* BC(char *a, int C){     
	int i = 0 , j = 0;
	char *s_buffer , hex;
	s_buffer = malloc(4*sizeof(char));
	//shift
	a = TC(a , C);
	//P128
	P128(a);
	//S0
	for(i = 0 ; i<128 ; i+=4){
		for(j = i ; j<i+4 ; j++)
			s_buffer[j-i] = a[j];   // s_buffer has the first 4 bits from the array a[]
		hex = bin_to_hex(s_buffer);
		hex = S0(hex);				//egine to s0
		hex_to_bin(hex , s_buffer);  //metatroph pali se binary
		for(j = i ; j<i+4 ; j++)    
			a[j] = s_buffer[j-i];
	}
	//P128
	P128(a);
	//shift
	a = TC(a , C);
	//epistrofh array
	free(s_buffer);
	return a;
}

char* E(char * a){   //krataei mono ta perrita bytetou kleidiou and returns an array with 64 positions
	char *temp;
	int counter = 0 , i=0 , j=0;
	temp = malloc(64*sizeof(char));
	for(i = 8 ; i<128 ; i+=16){
		for(j = i ; j<i+8 ; j++){
			temp[8*counter + (j-i)] = a[j];
		}
		counter++;
	}
	return temp;                  
}






char* f(char *a,int sel){   
int i = 0 , j = 0;
char *temp , temporary;

temp = malloc(64);

for(i = 0 ; i<64 ; i+=4){
	for(j = i ; j<i+4 ; j++){
		if(sel == 0)
		{
			if((j-i) == 0 || (j-i) == 2)
				temp[j] = xor(a[j] , a[j+1]);
			else
				temp[j] = a[j];		
		}
		else if(sel == 1)
		{
			if((j-i) == 0 || (j-i) == 2){
				temporary = xor( a[j] , a[j+1] );
				if((j-i) == 0)
					temp[j] = xor(temporary , a[j+2]);
				else
					temp[j] = xor(temporary , a[j-2]);
			}
			else{
				if((j-i) == 1)
					temp[j] = xor(a[j] , a[j+1] );
				else
					temp[j] = xor(a[j] , a[j-3] );
			}
		}
		
	}
}
for(i = 0 ; i<64 ; i++)
	a[i] = temp[i];
free(temp);	
return a;

}

	
	
	
	
	
