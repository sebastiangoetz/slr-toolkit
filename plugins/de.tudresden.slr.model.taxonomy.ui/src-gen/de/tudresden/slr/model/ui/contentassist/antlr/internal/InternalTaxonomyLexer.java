package de.tudresden.slr.model.ui.contentassist.antlr.internal;

// Hack: Use our own Lexer superclass by means of import. 
// Currently there is no other way to specify the superclass for the lexer.
import org.eclipse.xtext.ui.editor.contentassist.antlr.internal.Lexer;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

@SuppressWarnings("all")
public class InternalTaxonomyLexer extends Lexer {
    public static final int T__9=9;
    public static final int T__8=8;
    public static final int RULE_ID=4;
    public static final int RULE_WS=6;
    public static final int RULE_NEWLINE=7;
    public static final int RULE_ML_COMMENT=5;
    public static final int EOF=-1;
    public static final int T__10=10;

    // delegates
    // delegators

    public InternalTaxonomyLexer() {;} 
    public InternalTaxonomyLexer(CharStream input) {
        this(input, new RecognizerSharedState());
    }
    public InternalTaxonomyLexer(CharStream input, RecognizerSharedState state) {
        super(input,state);

    }
    public String getGrammarFileName() { return "../de.tudresden.slr.model.taxonomy.ui/src-gen/de/tudresden/slr/model/ui/contentassist/antlr/internal/InternalTaxonomy.g"; }

    // $ANTLR start "T__8"
    public final void mT__8() throws RecognitionException {
        try {
            int _type = T__8;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../de.tudresden.slr.model.taxonomy.ui/src-gen/de/tudresden/slr/model/ui/contentassist/antlr/internal/InternalTaxonomy.g:11:6: ( '{' )
            // ../de.tudresden.slr.model.taxonomy.ui/src-gen/de/tudresden/slr/model/ui/contentassist/antlr/internal/InternalTaxonomy.g:11:8: '{'
            {
            match('{'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__8"

    // $ANTLR start "T__9"
    public final void mT__9() throws RecognitionException {
        try {
            int _type = T__9;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../de.tudresden.slr.model.taxonomy.ui/src-gen/de/tudresden/slr/model/ui/contentassist/antlr/internal/InternalTaxonomy.g:12:6: ( '}' )
            // ../de.tudresden.slr.model.taxonomy.ui/src-gen/de/tudresden/slr/model/ui/contentassist/antlr/internal/InternalTaxonomy.g:12:8: '}'
            {
            match('}'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__9"

    // $ANTLR start "T__10"
    public final void mT__10() throws RecognitionException {
        try {
            int _type = T__10;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../de.tudresden.slr.model.taxonomy.ui/src-gen/de/tudresden/slr/model/ui/contentassist/antlr/internal/InternalTaxonomy.g:13:7: ( ',' )
            // ../de.tudresden.slr.model.taxonomy.ui/src-gen/de/tudresden/slr/model/ui/contentassist/antlr/internal/InternalTaxonomy.g:13:9: ','
            {
            match(','); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__10"

    // $ANTLR start "RULE_ML_COMMENT"
    public final void mRULE_ML_COMMENT() throws RecognitionException {
        try {
            int _type = RULE_ML_COMMENT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../de.tudresden.slr.model.taxonomy.ui/src-gen/de/tudresden/slr/model/ui/contentassist/antlr/internal/InternalTaxonomy.g:385:17: ( '/*' ( options {greedy=false; } : . )* '*/' )
            // ../de.tudresden.slr.model.taxonomy.ui/src-gen/de/tudresden/slr/model/ui/contentassist/antlr/internal/InternalTaxonomy.g:385:19: '/*' ( options {greedy=false; } : . )* '*/'
            {
            match("/*"); 

            // ../de.tudresden.slr.model.taxonomy.ui/src-gen/de/tudresden/slr/model/ui/contentassist/antlr/internal/InternalTaxonomy.g:385:24: ( options {greedy=false; } : . )*
            loop1:
            do {
                int alt1=2;
                int LA1_0 = input.LA(1);

                if ( (LA1_0=='*') ) {
                    int LA1_1 = input.LA(2);

                    if ( (LA1_1=='/') ) {
                        alt1=2;
                    }
                    else if ( ((LA1_1>='\u0000' && LA1_1<='.')||(LA1_1>='0' && LA1_1<='\uFFFF')) ) {
                        alt1=1;
                    }


                }
                else if ( ((LA1_0>='\u0000' && LA1_0<=')')||(LA1_0>='+' && LA1_0<='\uFFFF')) ) {
                    alt1=1;
                }


                switch (alt1) {
            	case 1 :
            	    // ../de.tudresden.slr.model.taxonomy.ui/src-gen/de/tudresden/slr/model/ui/contentassist/antlr/internal/InternalTaxonomy.g:385:52: .
            	    {
            	    matchAny(); 

            	    }
            	    break;

            	default :
            	    break loop1;
                }
            } while (true);

            match("*/"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "RULE_ML_COMMENT"

    // $ANTLR start "RULE_WS"
    public final void mRULE_WS() throws RecognitionException {
        try {
            int _type = RULE_WS;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../de.tudresden.slr.model.taxonomy.ui/src-gen/de/tudresden/slr/model/ui/contentassist/antlr/internal/InternalTaxonomy.g:387:9: ( ( ' ' | '\\t' )+ )
            // ../de.tudresden.slr.model.taxonomy.ui/src-gen/de/tudresden/slr/model/ui/contentassist/antlr/internal/InternalTaxonomy.g:387:11: ( ' ' | '\\t' )+
            {
            // ../de.tudresden.slr.model.taxonomy.ui/src-gen/de/tudresden/slr/model/ui/contentassist/antlr/internal/InternalTaxonomy.g:387:11: ( ' ' | '\\t' )+
            int cnt2=0;
            loop2:
            do {
                int alt2=2;
                int LA2_0 = input.LA(1);

                if ( (LA2_0=='\t'||LA2_0==' ') ) {
                    alt2=1;
                }


                switch (alt2) {
            	case 1 :
            	    // ../de.tudresden.slr.model.taxonomy.ui/src-gen/de/tudresden/slr/model/ui/contentassist/antlr/internal/InternalTaxonomy.g:
            	    {
            	    if ( input.LA(1)=='\t'||input.LA(1)==' ' ) {
            	        input.consume();

            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;}


            	    }
            	    break;

            	default :
            	    if ( cnt2 >= 1 ) break loop2;
                        EarlyExitException eee =
                            new EarlyExitException(2, input);
                        throw eee;
                }
                cnt2++;
            } while (true);


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "RULE_WS"

    // $ANTLR start "RULE_NEWLINE"
    public final void mRULE_NEWLINE() throws RecognitionException {
        try {
            int _type = RULE_NEWLINE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../de.tudresden.slr.model.taxonomy.ui/src-gen/de/tudresden/slr/model/ui/contentassist/antlr/internal/InternalTaxonomy.g:389:14: ( ( '\\r' )? '\\n' )
            // ../de.tudresden.slr.model.taxonomy.ui/src-gen/de/tudresden/slr/model/ui/contentassist/antlr/internal/InternalTaxonomy.g:389:16: ( '\\r' )? '\\n'
            {
            // ../de.tudresden.slr.model.taxonomy.ui/src-gen/de/tudresden/slr/model/ui/contentassist/antlr/internal/InternalTaxonomy.g:389:16: ( '\\r' )?
            int alt3=2;
            int LA3_0 = input.LA(1);

            if ( (LA3_0=='\r') ) {
                alt3=1;
            }
            switch (alt3) {
                case 1 :
                    // ../de.tudresden.slr.model.taxonomy.ui/src-gen/de/tudresden/slr/model/ui/contentassist/antlr/internal/InternalTaxonomy.g:389:16: '\\r'
                    {
                    match('\r'); 

                    }
                    break;

            }

            match('\n'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "RULE_NEWLINE"

    // $ANTLR start "RULE_ID"
    public final void mRULE_ID() throws RecognitionException {
        try {
            int _type = RULE_ID;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../de.tudresden.slr.model.taxonomy.ui/src-gen/de/tudresden/slr/model/ui/contentassist/antlr/internal/InternalTaxonomy.g:391:9: ( ( 'A' .. 'Z' | 'a' .. 'z' ) ( 'A' .. 'Z' | 'a' .. 'z' | '_' | '-' | '0' .. '9' | ' ' )* )
            // ../de.tudresden.slr.model.taxonomy.ui/src-gen/de/tudresden/slr/model/ui/contentassist/antlr/internal/InternalTaxonomy.g:391:11: ( 'A' .. 'Z' | 'a' .. 'z' ) ( 'A' .. 'Z' | 'a' .. 'z' | '_' | '-' | '0' .. '9' | ' ' )*
            {
            if ( (input.LA(1)>='A' && input.LA(1)<='Z')||(input.LA(1)>='a' && input.LA(1)<='z') ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            // ../de.tudresden.slr.model.taxonomy.ui/src-gen/de/tudresden/slr/model/ui/contentassist/antlr/internal/InternalTaxonomy.g:391:31: ( 'A' .. 'Z' | 'a' .. 'z' | '_' | '-' | '0' .. '9' | ' ' )*
            loop4:
            do {
                int alt4=2;
                int LA4_0 = input.LA(1);

                if ( (LA4_0==' '||LA4_0=='-'||(LA4_0>='0' && LA4_0<='9')||(LA4_0>='A' && LA4_0<='Z')||LA4_0=='_'||(LA4_0>='a' && LA4_0<='z')) ) {
                    alt4=1;
                }


                switch (alt4) {
            	case 1 :
            	    // ../de.tudresden.slr.model.taxonomy.ui/src-gen/de/tudresden/slr/model/ui/contentassist/antlr/internal/InternalTaxonomy.g:
            	    {
            	    if ( input.LA(1)==' '||input.LA(1)=='-'||(input.LA(1)>='0' && input.LA(1)<='9')||(input.LA(1)>='A' && input.LA(1)<='Z')||input.LA(1)=='_'||(input.LA(1)>='a' && input.LA(1)<='z') ) {
            	        input.consume();

            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;}


            	    }
            	    break;

            	default :
            	    break loop4;
                }
            } while (true);


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "RULE_ID"

    public void mTokens() throws RecognitionException {
        // ../de.tudresden.slr.model.taxonomy.ui/src-gen/de/tudresden/slr/model/ui/contentassist/antlr/internal/InternalTaxonomy.g:1:8: ( T__8 | T__9 | T__10 | RULE_ML_COMMENT | RULE_WS | RULE_NEWLINE | RULE_ID )
        int alt5=7;
        switch ( input.LA(1) ) {
        case '{':
            {
            alt5=1;
            }
            break;
        case '}':
            {
            alt5=2;
            }
            break;
        case ',':
            {
            alt5=3;
            }
            break;
        case '/':
            {
            alt5=4;
            }
            break;
        case '\t':
        case ' ':
            {
            alt5=5;
            }
            break;
        case '\n':
        case '\r':
            {
            alt5=6;
            }
            break;
        case 'A':
        case 'B':
        case 'C':
        case 'D':
        case 'E':
        case 'F':
        case 'G':
        case 'H':
        case 'I':
        case 'J':
        case 'K':
        case 'L':
        case 'M':
        case 'N':
        case 'O':
        case 'P':
        case 'Q':
        case 'R':
        case 'S':
        case 'T':
        case 'U':
        case 'V':
        case 'W':
        case 'X':
        case 'Y':
        case 'Z':
        case 'a':
        case 'b':
        case 'c':
        case 'd':
        case 'e':
        case 'f':
        case 'g':
        case 'h':
        case 'i':
        case 'j':
        case 'k':
        case 'l':
        case 'm':
        case 'n':
        case 'o':
        case 'p':
        case 'q':
        case 'r':
        case 's':
        case 't':
        case 'u':
        case 'v':
        case 'w':
        case 'x':
        case 'y':
        case 'z':
            {
            alt5=7;
            }
            break;
        default:
            NoViableAltException nvae =
                new NoViableAltException("", 5, 0, input);

            throw nvae;
        }

        switch (alt5) {
            case 1 :
                // ../de.tudresden.slr.model.taxonomy.ui/src-gen/de/tudresden/slr/model/ui/contentassist/antlr/internal/InternalTaxonomy.g:1:10: T__8
                {
                mT__8(); 

                }
                break;
            case 2 :
                // ../de.tudresden.slr.model.taxonomy.ui/src-gen/de/tudresden/slr/model/ui/contentassist/antlr/internal/InternalTaxonomy.g:1:15: T__9
                {
                mT__9(); 

                }
                break;
            case 3 :
                // ../de.tudresden.slr.model.taxonomy.ui/src-gen/de/tudresden/slr/model/ui/contentassist/antlr/internal/InternalTaxonomy.g:1:20: T__10
                {
                mT__10(); 

                }
                break;
            case 4 :
                // ../de.tudresden.slr.model.taxonomy.ui/src-gen/de/tudresden/slr/model/ui/contentassist/antlr/internal/InternalTaxonomy.g:1:26: RULE_ML_COMMENT
                {
                mRULE_ML_COMMENT(); 

                }
                break;
            case 5 :
                // ../de.tudresden.slr.model.taxonomy.ui/src-gen/de/tudresden/slr/model/ui/contentassist/antlr/internal/InternalTaxonomy.g:1:42: RULE_WS
                {
                mRULE_WS(); 

                }
                break;
            case 6 :
                // ../de.tudresden.slr.model.taxonomy.ui/src-gen/de/tudresden/slr/model/ui/contentassist/antlr/internal/InternalTaxonomy.g:1:50: RULE_NEWLINE
                {
                mRULE_NEWLINE(); 

                }
                break;
            case 7 :
                // ../de.tudresden.slr.model.taxonomy.ui/src-gen/de/tudresden/slr/model/ui/contentassist/antlr/internal/InternalTaxonomy.g:1:63: RULE_ID
                {
                mRULE_ID(); 

                }
                break;

        }

    }


 

}