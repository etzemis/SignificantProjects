#include<stdio.h>
#include"files.h"
 
char* iceberg_encrypt(char* key,char* m,int N)         // key = 128   and m = 64
{
char *temp_key , *sel_key , *temp , *c;
int i;
	// ROUND 0
	temp_key = key;
	sel_key = f(E(key),1);
	temp = SK(m,sel_key);
	// ROUNDS 1 - N/2
	for(i=0;i<(N/2);i++)
	{
		temp_key = BC(temp_key,0);
		sel_key = f(E(temp_key),1);
		temp = EK(gama(temp),sel_key);
	}
	// ROUNDS N/2+1 - N-1
	for(i=0;i<(N/2)-1;i++)
	{
		temp_key = BC(temp_key,1);
		sel_key = f(E(temp_key),1);
		temp = EK(gama(temp),sel_key);
	}
	// ROUND N
	temp_key = BC(temp_key,1);
	sel_key = f(E(temp_key),0);
	c = SK(gama(temp),sel_key);
	return c;
}


char* iceberg_decrypt(char* key,char *c, int N)
{
char *temp_key , *sel_key , *temp ,*m;
int i;
  // ROUND 0
  temp_key = key;
  sel_key = f(E(key),0);
  temp = gama(SK(c,sel_key));
  // ROUNDS 1 - N/2
  for(i=0;i<(N/2);i++)
  {
    temp_key = BC(temp_key,0);
    sel_key = f(E(temp_key),0);
    temp = gama(EK(temp,sel_key));
  }
  // ROUNDS N/2+1 - N-1
  for(i=0;i<(N/2)-1;i++)
  {
    temp_key = BC(temp_key,1);
    sel_key = f(E(temp_key),0);
    temp = gama(EK(temp,sel_key));
  }
  // ROUND N
  temp_key = BC(temp_key,1);
  sel_key = f(E(temp_key),1);
  m = SK(temp,sel_key);
  return m;
}

