// main.c
#include<stdio.h>
#include<string.h>
#include<ctype.h>

char getnbc(){
	char c;
	while(isspace(c = getchar())){
	}
	return c;
}

int isKey(char str[]){
	if(strcmp(str, "if") == 0){
		printf("If\n");
		return 1;
	}
	else if(strcmp(str, "else") == 0){
		printf("Else\n");
		return 1;
	}
	else if(strcmp(str, "while") == 0){
		printf("While\n");
		return 1;
	}
	else if(strcmp(str, "break") == 0){
		printf("Break\n");
		return 1;
	}
	else if(strcmp(str, "continue") == 0){
		printf("Continue\n");
		return 1;
	}
	else if(strcmp(str, "return") == 0){
		printf("Return\n");
		return 1;
	}
	return 0;
}

int isunderline(char c){
	if(c == '_'){
		return 1;
	}
	return 0;
}

int isident(char c){
	if(isdigit(c) || isalpha(c) || isunderline(c)){
		return 1;
	}
	return 0;
}

int main(){
	char c, token[100], temp;
	int i;
	c = getnbc();
	while(1){
		memset(token, 0, sizeof(token));
		i = 1;
		//Ident?
		if(isalpha(c) || isunderline(c)){
			token[0] = c;
			while(isident(c=getchar())){
				token[i++] = c;
			}
			//one more character
			if(isspace(c)){
				c = getnbc();
			}
			if(!isKey(token)){
				printf("Ident(%s)\n", token);
			}
		}
		//Number?
		else if(isdigit(c)){
			token[0] = c;
			while(isdigit(c=getchar())){
				token[i++] = c;
			}
			//again
			if(isspace(c)){
				c = getnbc();
			}
			printf("Number(%s)\n", token);
		}
		//Eq or Assign?
		else if(c == '=' ){
			temp = getchar();
			if(temp == '='){
				printf("Eq\n");
				c = getnbc();
			}
			else{
				printf("Assign\n");
				if(isspace(temp)){
					c = getnbc();
				}
				else c = temp;
			}
		}
		//Others?
		else if(c == ';'){
			printf("Semicolon\n");
			c = getnbc();
		}
		else if(c == '('){
			printf("LPar\n");
			c = getnbc();
		}
		else if(c == ')'){
			printf("RPar\n");
			c = getnbc();
		}
		else if(c == '{'){
			printf("LBrace\n");
			c = getnbc();
		}
		else if(c == '}'){
			printf("RBrace\n");
			c = getnbc();
		}
		else if(c == '+'){
			printf("Plus\n");
			c = getnbc();
		}
		else if(c == '*'){
			printf("Mult\n");
			c = getnbc();
		}
		else if(c == '/'){
			printf("Div\n");
			c = getnbc();
		}
		else if(c == '<'){
			printf("Lt\n");
			c = getnbc();
		}
		else if(c == '>'){
			printf("Gt\n");
			c = getnbc();
		}
		else{
			if(c == -1){
				return 0;
			}
			printf("Err\n");
			return 0;
		}
		
	}
	return 0;
}	
