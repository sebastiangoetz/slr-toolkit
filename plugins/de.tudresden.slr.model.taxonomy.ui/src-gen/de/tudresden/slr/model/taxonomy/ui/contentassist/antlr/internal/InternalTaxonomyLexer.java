package de.tudresden.slr.model.taxonomy.ui.contentassist.antlr.internal;

import org.antlr.runtime.BaseRecognizer;
import org.antlr.runtime.CharStream;
import org.antlr.runtime.DFA;
import org.antlr.runtime.EarlyExitException;
import org.antlr.runtime.MismatchedSetException;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.RecognizerSharedState;
// Hack: Use our own Lexer superclass by means of import. 
// Currently there is no other way to specify the superclass for the lexer.
import org.eclipse.xtext.ui.editor.contentassist.antlr.internal.Lexer;

@SuppressWarnings("all")
public class InternalTaxonomyLexer extends Lexer {
    public static final int T__9=9;
    public static final int RULE_ID=4;
    public static final int RULE_WS=7;
    public static final int RULE_NEWLINE=8;
    public static final int RULE_SL_COMMENT=5;
    public static final int T__11=11;
    public static final int RULE_ML_COMMENT=6;
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
    public String getGrammarFileName() { return "InternalTaxonomy.g"; }

    // $ANTLR start "T__9"
    public final void mT__9() throws RecognitionException {
        try {
            int _type = T__9;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalTaxonomy.g:11:6: ( ',' )
            // InternalTaxonomy.g:11:8: ','
            {
            match(','); 

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
            // InternalTaxonomy.g:12:7: ( '{' )
            // InternalTaxonomy.g:12:9: '{'
            {
            match('{'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__10"

    // $ANTLR start "T__11"
    public final void mT__11() throws RecognitionException {
        try {
            int _type = T__11;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalTaxonomy.g:13:7: ( '}' )
            // InternalTaxonomy.g:13:9: '}'
            {
            match('}'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__11"

    // $ANTLR start "RULE_SL_COMMENT"
    public final void mRULE_SL_COMMENT() throws RecognitionException {
        try {
            int _type = RULE_SL_COMMENT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalTaxonomy.g:600:17: ( '//' )
            // InternalTaxonomy.g:600:19: '//'
            {
            match("//"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "RULE_SL_COMMENT"

    // $ANTLR start "RULE_ML_COMMENT"
    public final void mRULE_ML_COMMENT() throws RecognitionException {
        try {
            int _type = RULE_ML_COMMENT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalTaxonomy.g:602:17: ( '/*' ( options {greedy=false; } : . )* '*/' )
            // InternalTaxonomy.g:602:19: '/*' ( options {greedy=false; } : . )* '*/'
            {
            match("/*"); 

            // InternalTaxonomy.g:602:24: ( options {greedy=false; } : . )*
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
            	    // InternalTaxonomy.g:602:52: .
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
            // InternalTaxonomy.g:604:9: ( ( ' ' | '\\t' )+ )
            // InternalTaxonomy.g:604:11: ( ' ' | '\\t' )+
            {
            // InternalTaxonomy.g:604:11: ( ' ' | '\\t' )+
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
            	    // InternalTaxonomy.g:
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
            // InternalTaxonomy.g:606:14: ( ( '\\r' )? '\\n' )
            // InternalTaxonomy.g:606:16: ( '\\r' )? '\\n'
            {
            // InternalTaxonomy.g:606:16: ( '\\r' )?
            int alt3=2;
            int LA3_0 = input.LA(1);

            if ( (LA3_0=='\r') ) {
                alt3=1;
            }
            switch (alt3) {
                case 1 :
                    // InternalTaxonomy.g:606:16: '\\r'
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
            // InternalTaxonomy.g:608:9: ( ( 'A' .. 'Z' | 'a' .. 'z' ) ( 'A' .. 'Z' | 'a' .. 'z' | '_' | '-' | '0' .. '9' | ' ' )* )
            // InternalTaxonomy.g:608:11: ( 'A' .. 'Z' | 'a' .. 'z' ) ( 'A' .. 'Z' | 'a' .. 'z' | '_' | '-' | '0' .. '9' | ' ' )*
            {
            if ( (input.LA(1)>='A' && input.LA(1)<='Z')||(input.LA(1)>='a' && input.LA(1)<='z') ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            // InternalTaxonomy.g:608:31: ( 'A' .. 'Z' | 'a' .. 'z' | '_' | '-' | '0' .. '9' | ' ' )*
            loop4:
            do {
                int alt4=2;
                int LA4_0 = input.LA(1);

                if ( (LA4_0==' '||LA4_0=='-'||(LA4_0>='0' && LA4_0<='9')||(LA4_0>='A' && LA4_0<='Z')||LA4_0=='_'||(LA4_0>='a' && LA4_0<='z')) ) {
                    alt4=1;
                }


                switch (alt4) {
            	case 1 :
            	    // InternalTaxonomy.g:
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
        // InternalTaxonomy.g:1:8: ( T__9 | T__10 | T__11 | RULE_SL_COMMENT | RULE_ML_COMMENT | RULE_WS | RULE_NEWLINE | RULE_ID )
        int alt5=8;
        alt5 = dfa5.predict(input);
        switch (alt5) {
            case 1 :
                // InternalTaxonomy.g:1:10: T__9
                {
                mT__9(); 

                }
                break;
            case 2 :
                // InternalTaxonomy.g:1:15: T__10
                {
                mT__10(); 

                }
                break;
            case 3 :
                // InternalTaxonomy.g:1:21: T__11
                {
                mT__11(); 

                }
                break;
            case 4 :
                // InternalTaxonomy.g:1:27: RULE_SL_COMMENT
                {
                mRULE_SL_COMMENT(); 

                }
                break;
            case 5 :
                // InternalTaxonomy.g:1:43: RULE_ML_COMMENT
                {
                mRULE_ML_COMMENT(); 

                }
                break;
            case 6 :
                // InternalTaxonomy.g:1:59: RULE_WS
                {
                mRULE_WS(); 

                }
                break;
            case 7 :
                // InternalTaxonomy.g:1:67: RULE_NEWLINE
                {
                mRULE_NEWLINE(); 

                }
                break;
            case 8 :
                // InternalTaxonomy.g:1:80: RULE_ID
                {
                mRULE_ID(); 

                }
                break;

        }

    }


    protected DFA5 dfa5 = new DFA5(this);
    static final String DFA5_eotS =
        "\12\uffff";
    static final String DFA5_eofS =
        "\12\uffff";
    static final String DFA5_minS =
        "\1\11\3\uffff\1\52\5\uffff";
    static final String DFA5_maxS =
        "\1\175\3\uffff\1\57\5\uffff";
    static final String DFA5_acceptS =
        "\1\uffff\1\1\1\2\1\3\1\uffff\1\6\1\7\1\10\1\4\1\5";
    static final String DFA5_specialS =
        "\12\uffff}>";
    static final String[] DFA5_transitionS = DFA5_transitionS_.DFA5_transitionS;
    private static final class DFA5_transitionS_ {
        static final String[] DFA5_transitionS = {
                "\1\5\1\6\2\uffff\1\6\22\uffff\1\5\13\uffff\1\1\2\uffff\1\4\21\uffff\32\7\6\uffff\32\7\1\2\1\uffff\1\3",
                "",
                "",
                "",
                "\1\11\4\uffff\1\10",
                "",
                "",
                "",
                "",
                ""
        };
    }

    static final short[] DFA5_eot = DFA.unpackEncodedString(DFA5_eotS);
    static final short[] DFA5_eof = DFA.unpackEncodedString(DFA5_eofS);
    static final char[] DFA5_min = DFA.unpackEncodedStringToUnsignedChars(DFA5_minS);
    static final char[] DFA5_max = DFA.unpackEncodedStringToUnsignedChars(DFA5_maxS);
    static final short[] DFA5_accept = DFA.unpackEncodedString(DFA5_acceptS);
    static final short[] DFA5_special = DFA.unpackEncodedString(DFA5_specialS);
    static final short[][] DFA5_transition;

    static {
        int numStates = DFA5_transitionS.length;
        DFA5_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA5_transition[i] = DFA.unpackEncodedString(DFA5_transitionS[i]);
        }
    }

    class DFA5 extends DFA {

        public DFA5(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 5;
            this.eot = DFA5_eot;
            this.eof = DFA5_eof;
            this.min = DFA5_min;
            this.max = DFA5_max;
            this.accept = DFA5_accept;
            this.special = DFA5_special;
            this.transition = DFA5_transition;
        }
        public String getDescription() {
            return "1:1: Tokens : ( T__9 | T__10 | T__11 | RULE_SL_COMMENT | RULE_ML_COMMENT | RULE_WS | RULE_NEWLINE | RULE_ID );";
        }
    }
 

}