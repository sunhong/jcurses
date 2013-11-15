#include "include/jcurses_system_Toolkit.h"
#include <curses.h>
#include <stdio.h>

#define JCURSES_ATTRIBUTES(number,att) (has_colors())?(att|COLOR_PAIR(number+1)):((number==0)?A_NORMAL:((number==1)?A_REVERSE:A_BOLD))

FILE * logStream = NULL;

void initLog() {
	logStream = fopen("njcurses.log","a");
	fprintf(logStream, "native logging initialized!\n");
	fflush(logStream);
}

static int paintingAction = 0;

void endPainting() {
	if (!paintingAction) {
		move(0,0);
		refresh();
	}
}

JNIEXPORT jint JNICALL Java_jcurses_system_Toolkit_getScreenWidth (JNIEnv * env, jclass class) {
	return COLS;
}


JNIEXPORT jint JNICALL Java_jcurses_system_Toolkit_getScreenHeight (JNIEnv * env, jclass class) {
	return LINES
	;
}


JNIEXPORT jint JNICALL Java_jcurses_system_Toolkit_hasColorsAsInteger (JNIEnv * env, jclass class) {
	return has_colors();
}

JNIEXPORT void JNICALL Java_jcurses_system_Toolkit_fillAttributes (JNIEnv * env , jclass class, jlongArray attributes) {
	jlong attrs[3];
	attrs[0] = A_NORMAL;
	attrs[1] = A_REVERSE;
	attrs[2] = A_BOLD;
	(*env)->SetLongArrayRegion(env,attributes,0,3,attrs);
}

JNIEXPORT void JNICALL Java_jcurses_system_Toolkit_fillBasicColors (JNIEnv * env , jclass class, jshortArray basicColors) {
	short colors[8];
	colors[0] = COLOR_BLACK;
	colors[1] = COLOR_RED;
	colors[2] = COLOR_GREEN;
	colors[3] = COLOR_YELLOW;
	colors[4] = COLOR_BLUE;
	colors[5] = COLOR_MAGENTA;
	colors[6] = COLOR_CYAN;
	colors[7] = COLOR_WHITE;
	(*env)->SetShortArrayRegion(env,basicColors,0,8,colors);
}


JNIEXPORT void JNICALL Java_jcurses_system_Toolkit_initColorPair (JNIEnv * env, jclass class, jshort background, 
																	 jshort foreground, jshort number) {
	init_pair(number+1,foreground, background);
}


jint computeChtype(jshort number) {
	/* Hardcoded NORMAL is fine here - computeChtype only used for pair */
	return JCURSES_ATTRIBUTES(number,A_NORMAL); 
}

JNIEXPORT jint JNICALL Java_jcurses_system_Toolkit_computeChtype (JNIEnv * env, jclass class, jshort number) {
	return computeChtype(number);
}

JNIEXPORT void JNICALL Java_jcurses_system_Toolkit_init (JNIEnv * env, jclass class) {
	//initLog();
	initscr ();	
	keypad (stdscr, TRUE);
	cbreak ();
	noecho ();
	start_color();
	/*curs_set(0);*/
}

void clear_box(int x, int y, int width, int height, jshort number, jlong attr) {
	int i, j;
	/*chtype background;
	/background = ((chtype)' ')|JCURSES_ATTRIBUTES(number);*/
	attrset(JCURSES_ATTRIBUTES(number,attr));
	for (i=x; i<(x+width); i++) {
	   for (j=y; j<(y+height);j++) {
		 move(j,i);
		 addch(' ');
	   }
	}
	endPainting();
}



JNIEXPORT void JNICALL Java_jcurses_system_Toolkit_clearScreen (JNIEnv * env, jclass class, jshort number, jlong attr) {
	clear_box(0,0,COLS,LINES,number,attr);
}


JNIEXPORT void JNICALL Java_jcurses_system_Toolkit_drawRectangle (JNIEnv * env , jclass class, jint x, jint y,
                                                                  jint width, jint height, jshort number, jlong attr) {
	clear_box(x, y, width,height,number,attr);
}


JNIEXPORT void JNICALL Java_jcurses_system_Toolkit_shutdown (JNIEnv * env, jclass class) {
	endwin();
}

void drawLineWithChars(jint start, jint end, jint end2, jshort number, jlong attribute, short alignment, chtype ch) {
	
	int x1, x2, i;
  /*Zeichnen vorbereiten*/
   
   attrset(JCURSES_ATTRIBUTES(number,attribute));
	
	/*Grenzen ermitteln*/ 
	if (start < end) {
		x1 = start;
		x2 = end;
	}  else {
		x1 = end;
		x2 = start;
	}
	
	/*Zeichnen*/ 
	
	for (i=x1; i<=x2; i++) {
		if (alignment) {
			/*horizontal*/
			move(end2, i);
			addch(ch);
		} else {
			/*vertikal*/
			move(i, end2);
			addch(ch);
		}
	}
	
	endPainting();
}

void drawLine(jint start, jint end, jint end2, jshort number, jlong attr, short alignment) {
	
	if (alignment) {
		/*horizontal*/
		drawLineWithChars(start, end, end2, number, attr, alignment, ACS_HLINE);
	} else {
	    drawLineWithChars(start, end, end2, number, attr, alignment, ACS_VLINE);	
	}
}



JNIEXPORT void JNICALL Java_jcurses_system_Toolkit_drawHorizontalLine (JNIEnv * env, jclass class, jint startX, jint startY
						  ,jint endX, jshort number, jlong attr) {
   drawLine(startX, endX, startY, number, attr, 1);
	
}

JNIEXPORT void JNICALL Java_jcurses_system_Toolkit_drawVerticalLine (JNIEnv * env, jclass class, jint startX, 
							 jint startY, jint endY, jshort number, jlong attr) {
	drawLine(startY, endY, startX, number, attr, 0);
}


void drawThickLine(jint start, jint end, jint end2, jshort number, jlong attr, short alignment) {
	drawLineWithChars(start, end, end2, number, attr, alignment, ACS_CKBOARD);
}


JNIEXPORT void JNICALL Java_jcurses_system_Toolkit_drawHorizontalThickLine (JNIEnv * env, jclass class, jint startX, jint startY
                                                                            ,jint endX, jshort number, jlong attr) {
   drawThickLine(startX, endX, startY, number, attr, 1);
	
}

JNIEXPORT void JNICALL Java_jcurses_system_Toolkit_drawVerticalThickLine (JNIEnv * env, jclass class, jint startX, 
                                                                          jint startY, jint endY, jshort number, jlong attr) {
	drawThickLine(startY, endY, startX, number, attr, 0);
}



JNIEXPORT void JNICALL Java_jcurses_system_Toolkit_drawCorner (JNIEnv * env, jclass class, jint x1, jint y1, 
                                                               jint x2, jint y2, jshort number, jlong attr, jshort alignment) {
	int cornerX, cornerY, otherX, otherY, character;
	/*Zeichnen vorbereiten*/
   
   	attrset(JCURSES_ATTRIBUTES(number,attr));
   	
	/*Zeichnen*/
	
	if ((x1 == x2) && (y1 == y2)) {
		switch (alignment) {
			case 2:
				character = ACS_LLCORNER;
				break;
			case 3:
				character = ACS_LRCORNER;
				break;
			case 4:
				character = ACS_ULCORNER;
				break;
			case 5:
				character = ACS_URCORNER;
				break;
			 default:
				character = ACS_LLCORNER;
				break;
		}
		move(y1, x1);
		addch(character);
	} else if (x1 == x2) {
	   drawLine(y1, y2, x1, number, attr, 0);
	}  else if (y1 == y2) {
	   drawLine(x1, x2, y1, number, attr, 1);
	}  else {
	   
	   if (alignment) {
	   	  if (y1 > y2) {
			  cornerY = y1;
			  otherY = y2;
			  cornerX = x2;
			  otherX = x1;
		  } else {
			  cornerY = y2;
			  otherY = y1;
			  cornerX = x1;
			  otherX = x2;
		  }
	   } else {
	   	  if (y1 > y2) {
			  cornerY = y2;
			  otherY = y1;
			  cornerX = x1;
			  otherX = x2;
		  } else {
			  cornerY = y1;
			  otherY = y2;
			  cornerX = x2;
			  otherX = x1;
		  } 
	   }
	   if ((cornerX < otherX) && (cornerY < otherY)) {
	   	  character = ACS_ULCORNER;
	   } else if ((cornerX > otherX) && (cornerY  < otherY)) {
		 character = ACS_URCORNER;  
	   } else if ((cornerX < otherX) && (cornerY  > otherY)) {
		 character = ACS_LLCORNER;  
	   } else if ((cornerX > otherX) && (cornerY  > otherY)) {
		 character = ACS_LRCORNER;  
	   }
	   
	   drawLine(cornerX, otherX, cornerY, number, attr, 1);
	   drawLine(cornerY, otherY, cornerX, number, attr, 0);
	   move(cornerY, cornerX);
	   addch(character);
	   
	}
	
	endPainting();
	
}




JNIEXPORT void JNICALL Java_jcurses_system_Toolkit_drawBorder(JNIEnv * env, jclass class, jint x, jint y, jint width, 
                                                              jint height, jshort number, jlong attr) {
   /*Zeichnen vorbereiten*/
   int i=0;
   
   attrset(JCURSES_ATTRIBUTES(number,attr));
   
   /*Obere Linke Ecke setzen*/ 
   move(y,x);
   addch(ACS_ULCORNER);
   
   /*Obere Seite ziehen*/
   
   for (i=1; i<(width-1); i++) {
   	 move(y,x+i);
	 addch(ACS_HLINE);
   }
   
   
   /*Obere Rechte Ecke setzen*/ 
   move(y,x+width-1);
   addch(ACS_URCORNER);
   
   /*Rechte Seite ziehen*/
   
   for (i=1; i<(height-1); i++) {
   	 move(y+i,x+width-1);
	 addch(ACS_VLINE);
   }
   
   
   /*Untere rechte Ecke setzen*/ 
   move(y+height-1,x+width-1);
   addch(ACS_LRCORNER);
   
   /*Untere Seite ziehen*/
   
   for (i=1; i<(width-1); i++) {
   	 move(y+height-1,x+width-1-i);
	 addch(ACS_HLINE);
   }
   
   
   /*Untere Linke Ecke setzen*/ 
   move(y+height-1,x);
   addch(ACS_LLCORNER);
   
   /*Linke Seite ziehen*/
   
   for (i=1; i<(height-1); i++) {
   	 move(y+height-1-i,x);
	 addch(ACS_VLINE);
   }
   
   endPainting();
   	
	
}


JNIEXPORT void JNICALL Java_jcurses_system_Toolkit_printString (JNIEnv * env, jclass class, jbyteArray bytes, 
                                                                jint x, jint y, jint width, jint height, jshort number, jlong attr) {
	int j=0;
	int xpos,ypos;
	int length = (*env)->GetArrayLength(env,bytes);
	unsigned char c;
	unsigned char * charArray = (*env)->GetByteArrayElements(env, bytes, NULL);

	
	attrset(JCURSES_ATTRIBUTES(number,attr));
	
	xpos=x-1;
	ypos=y;
	
	for (j=0; j<length; j++) {
		c = charArray[j];
		if (c!='\r') {
			xpos++;
			if ((xpos == x+width) && (c!='\n')) {
				xpos = x;
				ypos++;
				if (ypos == y+height) {
					break;
				}
			}
		
			move(ypos, xpos);
			/*Behandlung spezieller Zeichen*/
		
			if (c == '\r') {
			/*cannt be*/
			} else if (c == '\n') {
				if (ypos == (y+height-1)) {
					/*ignore*/
				} else {
					ypos++;
					xpos=x-1;
				}
			} else if (c == '\t') {
				addch(' ');
			} else {
				addch(c);
			}
		}
		
	}
	
	endPainting();
	
	// RELEASE ALLOCATED JNI MEMORY - Fix for Bug ID #3042758
	(*env)->ReleaseByteArrayElements(env, bytes, charArray, JNI_ABORT);
	
}


JNIEXPORT jint JNICALL Java_jcurses_system_Toolkit_readByte (JNIEnv * env, jclass class) {
	int code= wgetch(stdscr);
	return code;
}


JNIEXPORT jint JNICALL Java_jcurses_system_Toolkit_getSpecialKeyCode (JNIEnv * env, jclass class, jint code) {
	int result = KEY_MAX; 
	switch (code) {
		case 0401:
			result = KEY_MIN;
			break;
		case 0402:
			result = KEY_DOWN;
			break;
		case 0403:
			result = KEY_UP;
			break;
		case 0404:
			result = KEY_LEFT;
			break;
		case 0405:
			result = KEY_RIGHT;
			break;
		case 0406:
			result = KEY_HOME;
			break;
		case 0407:
			result = KEY_BACKSPACE;
			break;
		case 0410:
			result = KEY_F0;
			break;
		case 0411:
			result = KEY_F(1);
			break;
		case 0412:
			result = KEY_F(2);
			break;
		case 0413:
			result = KEY_F(3);
			break;
		case 0414:
			result = KEY_F(4);
			break;
		case 0415:
			result = KEY_F(5);
			break;
		case 0416:
			result = KEY_F(6);
			break;
		case 0417:
			result = KEY_F(7);
			break;
		case 0420:
			result = KEY_F(010);
			break;
		case 0421:
			result = KEY_F(011);
			break;
		case 0422:
			result = KEY_F(012);
			break;
		case 0423:
			result = KEY_F(013);
			break;
		case 0424:
			result = KEY_F(014);
			break;
			
		case 0510:
			result = KEY_DL;
			break;
		case 0511:
			result = KEY_IL;
			break;
		case 0512:
			result = KEY_DC;
			break;
		case 0513:
			result = KEY_IC;
			break;
		case 0514:
			result = KEY_EIC;
			break;
		case 0515:
			result = KEY_CLEAR;
			break;
		case 0516:
			result = KEY_EOS;
			break;
		case 0517:
			result = KEY_EOL;
			break;
		
		case 0520:
			result = KEY_SF;
			break;
		case 0521:
			result = KEY_SR;
			break;
		case 0522:
			result = KEY_NPAGE;
			break;
		case 0523:
			result = KEY_PPAGE;
			break;
		case 0524:
			result = KEY_STAB;
			break;
		case 0525:
			result = KEY_CTAB;
			break;
		case 0526:
			result = KEY_CATAB;
			break;
		case 0527:
			result = KEY_ENTER;
			break;
			
		case 0530:
			result = KEY_SRESET;
			break;
		case 0531:
			result = KEY_RESET;
			break;
		case 0532:
			result = KEY_PRINT;
			break;
		case 0533:
			result = KEY_LL;
			break;
		case 0534:
			result = KEY_A1;
			break;
		case 0535:
			result = KEY_A3;
			break;
		case 0536:
			result = KEY_B2;
			break;
		case 0537:
			result = KEY_C1;
			break;
			
			
		case 0540:
			result = KEY_C3;
			break;
		case 0541:
			result = KEY_BTAB;
			break;
		case 0542:
			result = KEY_BEG;
			break;
		case 0543:
			result = KEY_CANCEL;
			break;
		case 0544:
			result = KEY_CLOSE;
			break;
		case 0545:
			result = KEY_COMMAND;
			break;
		case 0546:
			result = KEY_COPY;
			break;
		case 0547:
			result = KEY_CREATE;
			break;
			
		case 0550:
			result = KEY_END;
			break;
		case 0551:
			result = KEY_EXIT;
			break;
		case 0552:
			result = KEY_FIND;
			break;
		case 0553:
			result = KEY_HELP;
			break;
		case 0554:
			result = KEY_MARK;
			break;
		case 0555:
			result = KEY_MESSAGE;
			break;
		case 0556:
			result = KEY_MOVE;
			break;
		case 0557:
			result = KEY_NEXT;
			break;
			
		case 0560:
			result = KEY_OPEN;
			break;
		case 0561:
			result = KEY_OPTIONS;
			break;
		case 0562:
			result = KEY_PREVIOUS;
			break;
		case 0563:
			result = KEY_REDO;
			break;
		case 0564:
			result = KEY_REFERENCE;
			break;
		case 0565:
			result = KEY_REFRESH;
			break;
		case 0566:
			result = KEY_REPLACE;
			break;
		case 0567:
			result = KEY_RESTART;
			break;
			
		case 0570:
			result = KEY_RESUME;
			break;
		case 0571:
			result = KEY_SAVE;
			break;
		case 0572:
			result = KEY_SBEG;
			break;
		case 0573:
			result = KEY_SCANCEL;
			break;
		case 0574:
			result = KEY_SCOMMAND;
			break;
		case 0575:
			result = KEY_SCOPY;
			break;
		case 0576:
			result = KEY_SCREATE;
			break;
		case 0577:
			result = KEY_SDC;
			break;
			
		case 0600:
			result = KEY_SDL;
			break;
		case 0601:
			result = KEY_SELECT;
			break;
		case 0602:
			result = KEY_SEND;
			break;
		case 0603:
			result = KEY_SEOL;
			break;
		case 0604:
			result = KEY_SEXIT;
			break;
		case 0605:
			result = KEY_SFIND;
			break;
		case 0606:
			result = KEY_SHELP;
			break;
		case 0607:
			result = KEY_SHOME;
			break;
		
		case 0610:
			result = KEY_SIC;
			break;
		case 0611:
			result = KEY_SLEFT;
			break;
		case 0612:
			result = KEY_SMESSAGE;
			break;
		case 0613:
			result = KEY_SMOVE;
			break;
		case 0614:
			result = KEY_SNEXT;
			break;
		case 0615:
			result = KEY_SOPTIONS;
			break;
		case 0616:
			result = KEY_SPREVIOUS;
			break;
		case 0617:
			result = KEY_SPRINT;
			break;
			
		case 0620:
			result = KEY_SREDO;
			break;
		case 0621:
			result = KEY_SRIGHT;
			break;
		case 0622:
			result = KEY_SREPLACE;
			break;
		case 0623:
			result = KEY_SRSUME;
			break;
		case 0624:
			result = KEY_SSAVE;
			break;
		case 0625:
			result = KEY_SSUSPEND;
			break;
		case 0626:
			result = KEY_SUNDO;
			break;
		case 0627:
			result = KEY_SUSPEND;
			break;
		case 0630:
			result = KEY_UNDO;
			break;
		case 777:
			result = KEY_MAX;
			break;
		default:
			result = KEY_MIN;
		
		    
	}
	
	return result;
}


JNIEXPORT void JNICALL Java_jcurses_system_Toolkit_changeColors (JNIEnv *env, jclass clazz, jint x, jint y, jint width, 
                                                                 jint height, jshort colorpair, jlong attr) {
	int i,j;
	chtype currentChar = 0;
	attrset(JCURSES_ATTRIBUTES(colorpair,attr));
	for (i=0; i<width; i++) {
		for (j=0;j<height; j++) {
			currentChar = (mvinch(y+j,x+i) & A_CHARTEXT);
			addch(currentChar);
		}
		
	}
	endPainting();
}


JNIEXPORT void JNICALL Java_jcurses_system_Toolkit_startPainting (JNIEnv * env, jclass clazz) {
	paintingAction++;
}


JNIEXPORT void JNICALL Java_jcurses_system_Toolkit_endPainting (JNIEnv * env , jclass clazz) {
	paintingAction--;
	if (paintingAction < 0) {
		paintingAction = 0;
	}
	endPainting();
}


JNIEXPORT void JNICALL Java_jcurses_system_Toolkit_beep (JNIEnv * env, jclass clazz) {
	beep();
}
