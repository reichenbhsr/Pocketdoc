--
-- PostgreSQL database dump
--

SET statement_timeout = 0;
SET lock_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;

--
-- Name: plpgsql; Type: EXTENSION; Schema: -; Owner: 
--

CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;


--
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


SET search_path = public, pg_catalog;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- Name: action_suggestion_descriptions; Type: TABLE; Schema: public; Owner: pocketdoc; Tablespace: 
--

CREATE TABLE action_suggestion_descriptions (
    id integer NOT NULL,
    description text,
    action_suggestion integer NOT NULL,
    language integer NOT NULL
);


ALTER TABLE public.action_suggestion_descriptions OWNER TO pocketdoc;

--
-- Name: action_suggestion_descriptions_id_seq; Type: SEQUENCE; Schema: public; Owner: pocketdoc
--

CREATE SEQUENCE action_suggestion_descriptions_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.action_suggestion_descriptions_id_seq OWNER TO pocketdoc;

--
-- Name: action_suggestion_descriptions_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: pocketdoc
--

ALTER SEQUENCE action_suggestion_descriptions_id_seq OWNED BY action_suggestion_descriptions.id;


--
-- Name: action_suggestions; Type: TABLE; Schema: public; Owner: pocketdoc; Tablespace: 
--

CREATE TABLE action_suggestions (
    id integer NOT NULL,
    name text
);


ALTER TABLE public.action_suggestions OWNER TO pocketdoc;

--
-- Name: action_suggestions_id_seq; Type: SEQUENCE; Schema: public; Owner: pocketdoc
--

CREATE SEQUENCE action_suggestions_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.action_suggestions_id_seq OWNER TO pocketdoc;

--
-- Name: action_suggestions_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: pocketdoc
--

ALTER SEQUENCE action_suggestions_id_seq OWNED BY action_suggestions.id;


--
-- Name: answers; Type: TABLE; Schema: public; Owner: pocketdoc; Tablespace: 
--

CREATE TABLE answers (
    id integer NOT NULL
);


ALTER TABLE public.answers OWNER TO pocketdoc;

--
-- Name: answers_id_seq; Type: SEQUENCE; Schema: public; Owner: pocketdoc
--

CREATE SEQUENCE answers_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.answers_id_seq OWNER TO pocketdoc;

--
-- Name: answers_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: pocketdoc
--

ALTER SEQUENCE answers_id_seq OWNED BY answers.id;


--
-- Name: answers_to_histories; Type: TABLE; Schema: public; Owner: pocketdoc; Tablespace: 
--

CREATE TABLE answers_to_histories (
    id integer NOT NULL,
    answer integer,
    history integer NOT NULL,
    question integer,
    followup integer
);


ALTER TABLE public.answers_to_histories OWNER TO pocketdoc;

--
-- Name: answers_to_histories_id_seq; Type: SEQUENCE; Schema: public; Owner: pocketdoc
--

CREATE SEQUENCE answers_to_histories_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.answers_to_histories_id_seq OWNER TO pocketdoc;

--
-- Name: answers_to_histories_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: pocketdoc
--

ALTER SEQUENCE answers_to_histories_id_seq OWNED BY answers_to_histories.id;


--
-- Name: answers_to_syndroms; Type: TABLE; Schema: public; Owner: pocketdoc; Tablespace: 
--

CREATE TABLE answers_to_syndroms (
    id integer NOT NULL,
    answer integer NOT NULL,
    syndrom integer NOT NULL
);


ALTER TABLE public.answers_to_syndroms OWNER TO pocketdoc;

--
-- Name: answers_to_syndroms_id_seq; Type: SEQUENCE; Schema: public; Owner: pocketdoc
--

CREATE SEQUENCE answers_to_syndroms_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.answers_to_syndroms_id_seq OWNER TO pocketdoc;

--
-- Name: answers_to_syndroms_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: pocketdoc
--

ALTER SEQUENCE answers_to_syndroms_id_seq OWNED BY answers_to_syndroms.id;


--
-- Name: diagnoses; Type: TABLE; Schema: public; Owner: pocketdoc; Tablespace: 
--

CREATE TABLE diagnoses (
    id integer NOT NULL,
    name text
);


ALTER TABLE public.diagnoses OWNER TO pocketdoc;

--
-- Name: diagnoses_id_seq; Type: SEQUENCE; Schema: public; Owner: pocketdoc
--

CREATE SEQUENCE diagnoses_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.diagnoses_id_seq OWNER TO pocketdoc;

--
-- Name: diagnoses_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: pocketdoc
--

ALTER SEQUENCE diagnoses_id_seq OWNED BY diagnoses.id;


--
-- Name: diagnosis_descriptions; Type: TABLE; Schema: public; Owner: pocketdoc; Tablespace: 
--

CREATE TABLE diagnosis_descriptions (
    id integer NOT NULL,
    description text,
    diagnosis integer NOT NULL,
    language integer NOT NULL
);


ALTER TABLE public.diagnosis_descriptions OWNER TO pocketdoc;

--
-- Name: diagnosis_descriptions_id_seq; Type: SEQUENCE; Schema: public; Owner: pocketdoc
--

CREATE SEQUENCE diagnosis_descriptions_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.diagnosis_descriptions_id_seq OWNER TO pocketdoc;

--
-- Name: diagnosis_descriptions_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: pocketdoc
--

ALTER SEQUENCE diagnosis_descriptions_id_seq OWNED BY diagnosis_descriptions.id;


--
-- Name: diagnosis_designations; Type: TABLE; Schema: public; Owner: pocketdoc; Tablespace: 
--

CREATE TABLE diagnosis_designations (
    id integer NOT NULL,
    designation text,
    diagnosis integer NOT NULL,
    language integer NOT NULL
);


ALTER TABLE public.diagnosis_designations OWNER TO pocketdoc;

--
-- Name: diagnosis_designations_id_seq; Type: SEQUENCE; Schema: public; Owner: pocketdoc
--

CREATE SEQUENCE diagnosis_designations_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.diagnosis_designations_id_seq OWNER TO pocketdoc;

--
-- Name: diagnosis_designations_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: pocketdoc
--

ALTER SEQUENCE diagnosis_designations_id_seq OWNED BY diagnosis_designations.id;


--
-- Name: followups; Type: TABLE; Schema: public; Owner: pocketdoc; Tablespace: 
--

CREATE TABLE followups (
    id integer NOT NULL,
    usr integer NOT NULL,
    diagnosis integer NOT NULL,
    action_suggestion integer NOT NULL,
    "timestamp" timestamp without time zone NOT NULL
);


ALTER TABLE public.followups OWNER TO pocketdoc;

--
-- Name: followups_id_seq; Type: SEQUENCE; Schema: public; Owner: pocketdoc
--

CREATE SEQUENCE followups_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.followups_id_seq OWNER TO pocketdoc;

--
-- Name: followups_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: pocketdoc
--

ALTER SEQUENCE followups_id_seq OWNED BY followups.id;


--
-- Name: histories; Type: TABLE; Schema: public; Owner: pocketdoc; Tablespace: 
--

CREATE TABLE histories (
    id integer NOT NULL,
    consecutive_questions integer NOT NULL,
    last_answer integer
);


ALTER TABLE public.histories OWNER TO pocketdoc;

--
-- Name: histories_id_seq; Type: SEQUENCE; Schema: public; Owner: pocketdoc
--

CREATE SEQUENCE histories_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.histories_id_seq OWNER TO pocketdoc;

--
-- Name: histories_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: pocketdoc
--

ALTER SEQUENCE histories_id_seq OWNED BY histories.id;


--
-- Name: languages; Type: TABLE; Schema: public; Owner: pocketdoc; Tablespace: 
--

CREATE TABLE languages (
    id integer NOT NULL,
    name text,
    code text
);


ALTER TABLE public.languages OWNER TO pocketdoc;

--
-- Name: languages_id_seq; Type: SEQUENCE; Schema: public; Owner: pocketdoc
--

CREATE SEQUENCE languages_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.languages_id_seq OWNER TO pocketdoc;

--
-- Name: languages_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: pocketdoc
--

ALTER SEQUENCE languages_id_seq OWNED BY languages.id;


--
-- Name: perfect_diagnosis_diagnoses_to_answers; Type: TABLE; Schema: public; Owner: pocketdoc; Tablespace: 
--

CREATE TABLE perfect_diagnosis_diagnoses_to_answers (
    id integer NOT NULL,
    diagnosis integer NOT NULL,
    answer integer NOT NULL
);


ALTER TABLE public.perfect_diagnosis_diagnoses_to_answers OWNER TO pocketdoc;

--
-- Name: perfect_diagnosis_diagnoses_to_answers_id_seq; Type: SEQUENCE; Schema: public; Owner: pocketdoc
--

CREATE SEQUENCE perfect_diagnosis_diagnoses_to_answers_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.perfect_diagnosis_diagnoses_to_answers_id_seq OWNER TO pocketdoc;

--
-- Name: perfect_diagnosis_diagnoses_to_answers_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: pocketdoc
--

ALTER SEQUENCE perfect_diagnosis_diagnoses_to_answers_id_seq OWNED BY perfect_diagnosis_diagnoses_to_answers.id;


--
-- Name: question_descriptions; Type: TABLE; Schema: public; Owner: pocketdoc; Tablespace: 
--

CREATE TABLE question_descriptions (
    id integer NOT NULL,
    description text,
    question integer NOT NULL,
    language integer NOT NULL
);


ALTER TABLE public.question_descriptions OWNER TO pocketdoc;

--
-- Name: question_descriptions_id_seq; Type: SEQUENCE; Schema: public; Owner: pocketdoc
--

CREATE SEQUENCE question_descriptions_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.question_descriptions_id_seq OWNER TO pocketdoc;

--
-- Name: question_descriptions_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: pocketdoc
--

ALTER SEQUENCE question_descriptions_id_seq OWNED BY question_descriptions.id;


--
-- Name: questions; Type: TABLE; Schema: public; Owner: pocketdoc; Tablespace: 
--

CREATE TABLE questions (
    id integer NOT NULL,
    name text,
    is_symptom boolean,
    answer_yes integer NOT NULL,
    answer_no integer NOT NULL,
    depends_on integer,
    force_dependent_asking boolean,
    type integer
);


ALTER TABLE public.questions OWNER TO pocketdoc;

--
-- Name: questions_id_seq; Type: SEQUENCE; Schema: public; Owner: pocketdoc
--

CREATE SEQUENCE questions_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.questions_id_seq OWNER TO pocketdoc;

--
-- Name: questions_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: pocketdoc
--

ALTER SEQUENCE questions_id_seq OWNED BY questions.id;


--
-- Name: score_distribution_answers_to_action_suggestions; Type: TABLE; Schema: public; Owner: pocketdoc; Tablespace: 
--

CREATE TABLE score_distribution_answers_to_action_suggestions (
    id integer NOT NULL,
    score integer NOT NULL,
    answer integer NOT NULL,
    action_suggestion integer NOT NULL
);


ALTER TABLE public.score_distribution_answers_to_action_suggestions OWNER TO pocketdoc;

--
-- Name: score_distribution_answers_to_action_suggestions_id_seq; Type: SEQUENCE; Schema: public; Owner: pocketdoc
--

CREATE SEQUENCE score_distribution_answers_to_action_suggestions_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.score_distribution_answers_to_action_suggestions_id_seq OWNER TO pocketdoc;

--
-- Name: score_distribution_answers_to_action_suggestions_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: pocketdoc
--

ALTER SEQUENCE score_distribution_answers_to_action_suggestions_id_seq OWNED BY score_distribution_answers_to_action_suggestions.id;


--
-- Name: score_distribution_answers_to_diagnoses; Type: TABLE; Schema: public; Owner: pocketdoc; Tablespace: 
--

CREATE TABLE score_distribution_answers_to_diagnoses (
    id integer NOT NULL,
    score integer NOT NULL,
    answer integer NOT NULL,
    diagnosis integer NOT NULL
);


ALTER TABLE public.score_distribution_answers_to_diagnoses OWNER TO pocketdoc;

--
-- Name: score_distribution_answers_to_diagnoses_id_seq; Type: SEQUENCE; Schema: public; Owner: pocketdoc
--

CREATE SEQUENCE score_distribution_answers_to_diagnoses_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.score_distribution_answers_to_diagnoses_id_seq OWNER TO pocketdoc;

--
-- Name: score_distribution_answers_to_diagnoses_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: pocketdoc
--

ALTER SEQUENCE score_distribution_answers_to_diagnoses_id_seq OWNED BY score_distribution_answers_to_diagnoses.id;


--
-- Name: score_distribution_syndroms_to_action_suggestions; Type: TABLE; Schema: public; Owner: pocketdoc; Tablespace: 
--

CREATE TABLE score_distribution_syndroms_to_action_suggestions (
    id integer NOT NULL,
    score integer NOT NULL,
    syndrom integer NOT NULL,
    action_suggestion integer NOT NULL
);


ALTER TABLE public.score_distribution_syndroms_to_action_suggestions OWNER TO pocketdoc;

--
-- Name: score_distribution_syndroms_to_action_suggestions_id_seq; Type: SEQUENCE; Schema: public; Owner: pocketdoc
--

CREATE SEQUENCE score_distribution_syndroms_to_action_suggestions_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.score_distribution_syndroms_to_action_suggestions_id_seq OWNER TO pocketdoc;

--
-- Name: score_distribution_syndroms_to_action_suggestions_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: pocketdoc
--

ALTER SEQUENCE score_distribution_syndroms_to_action_suggestions_id_seq OWNED BY score_distribution_syndroms_to_action_suggestions.id;


--
-- Name: settings; Type: TABLE; Schema: public; Owner: pocketdoc; Tablespace: 
--

CREATE TABLE settings (
    id integer NOT NULL,
    name text,
    value text NOT NULL,
    type integer,
    ord integer
);


ALTER TABLE public.settings OWNER TO pocketdoc;

--
-- Name: settings_id_seq; Type: SEQUENCE; Schema: public; Owner: pocketdoc
--

CREATE SEQUENCE settings_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.settings_id_seq OWNER TO pocketdoc;

--
-- Name: settings_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: pocketdoc
--

ALTER SEQUENCE settings_id_seq OWNED BY settings.id;


--
-- Name: syndroms; Type: TABLE; Schema: public; Owner: pocketdoc; Tablespace: 
--

CREATE TABLE syndroms (
    id integer NOT NULL,
    name text
);


ALTER TABLE public.syndroms OWNER TO pocketdoc;

--
-- Name: syndroms_id_seq; Type: SEQUENCE; Schema: public; Owner: pocketdoc
--

CREATE SEQUENCE syndroms_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.syndroms_id_seq OWNER TO pocketdoc;

--
-- Name: syndroms_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: pocketdoc
--

ALTER SEQUENCE syndroms_id_seq OWNED BY syndroms.id;


--
-- Name: users; Type: TABLE; Schema: public; Owner: pocketdoc; Tablespace: 
--

CREATE TABLE users (
    id integer NOT NULL,
    name text,
    password text,
    history integer NOT NULL,
    email text,
    gender integer,
    age_category integer,
    is_admin boolean,
    is_temporary boolean,
    lang integer,
    last_activity timestamp without time zone,
    password_restore_token text
);


ALTER TABLE public.users OWNER TO pocketdoc;

--
-- Name: users_id_seq; Type: SEQUENCE; Schema: public; Owner: pocketdoc
--

CREATE SEQUENCE users_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.users_id_seq OWNER TO pocketdoc;

--
-- Name: users_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: pocketdoc
--

ALTER SEQUENCE users_id_seq OWNED BY users.id;


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: pocketdoc
--

ALTER TABLE ONLY action_suggestion_descriptions ALTER COLUMN id SET DEFAULT nextval('action_suggestion_descriptions_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: pocketdoc
--

ALTER TABLE ONLY action_suggestions ALTER COLUMN id SET DEFAULT nextval('action_suggestions_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: pocketdoc
--

ALTER TABLE ONLY answers ALTER COLUMN id SET DEFAULT nextval('answers_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: pocketdoc
--

ALTER TABLE ONLY answers_to_histories ALTER COLUMN id SET DEFAULT nextval('answers_to_histories_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: pocketdoc
--

ALTER TABLE ONLY answers_to_syndroms ALTER COLUMN id SET DEFAULT nextval('answers_to_syndroms_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: pocketdoc
--

ALTER TABLE ONLY diagnoses ALTER COLUMN id SET DEFAULT nextval('diagnoses_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: pocketdoc
--

ALTER TABLE ONLY diagnosis_descriptions ALTER COLUMN id SET DEFAULT nextval('diagnosis_descriptions_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: pocketdoc
--

ALTER TABLE ONLY diagnosis_designations ALTER COLUMN id SET DEFAULT nextval('diagnosis_designations_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: pocketdoc
--

ALTER TABLE ONLY followups ALTER COLUMN id SET DEFAULT nextval('followups_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: pocketdoc
--

ALTER TABLE ONLY histories ALTER COLUMN id SET DEFAULT nextval('histories_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: pocketdoc
--

ALTER TABLE ONLY languages ALTER COLUMN id SET DEFAULT nextval('languages_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: pocketdoc
--

ALTER TABLE ONLY perfect_diagnosis_diagnoses_to_answers ALTER COLUMN id SET DEFAULT nextval('perfect_diagnosis_diagnoses_to_answers_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: pocketdoc
--

ALTER TABLE ONLY question_descriptions ALTER COLUMN id SET DEFAULT nextval('question_descriptions_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: pocketdoc
--

ALTER TABLE ONLY questions ALTER COLUMN id SET DEFAULT nextval('questions_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: pocketdoc
--

ALTER TABLE ONLY score_distribution_answers_to_action_suggestions ALTER COLUMN id SET DEFAULT nextval('score_distribution_answers_to_action_suggestions_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: pocketdoc
--

ALTER TABLE ONLY score_distribution_answers_to_diagnoses ALTER COLUMN id SET DEFAULT nextval('score_distribution_answers_to_diagnoses_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: pocketdoc
--

ALTER TABLE ONLY score_distribution_syndroms_to_action_suggestions ALTER COLUMN id SET DEFAULT nextval('score_distribution_syndroms_to_action_suggestions_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: pocketdoc
--

ALTER TABLE ONLY settings ALTER COLUMN id SET DEFAULT nextval('settings_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: pocketdoc
--

ALTER TABLE ONLY syndroms ALTER COLUMN id SET DEFAULT nextval('syndroms_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: pocketdoc
--

ALTER TABLE ONLY users ALTER COLUMN id SET DEFAULT nextval('users_id_seq'::regclass);


--
-- Name: action_suggestion_descriptions_pkey; Type: CONSTRAINT; Schema: public; Owner: pocketdoc; Tablespace: 
--

ALTER TABLE ONLY action_suggestion_descriptions
    ADD CONSTRAINT action_suggestion_descriptions_pkey PRIMARY KEY (id);


--
-- Name: action_suggestions_pkey; Type: CONSTRAINT; Schema: public; Owner: pocketdoc; Tablespace: 
--

ALTER TABLE ONLY action_suggestions
    ADD CONSTRAINT action_suggestions_pkey PRIMARY KEY (id);


--
-- Name: answers_pkey; Type: CONSTRAINT; Schema: public; Owner: pocketdoc; Tablespace: 
--

ALTER TABLE ONLY answers
    ADD CONSTRAINT answers_pkey PRIMARY KEY (id);


--
-- Name: answers_to_histories_pkey; Type: CONSTRAINT; Schema: public; Owner: pocketdoc; Tablespace: 
--

ALTER TABLE ONLY answers_to_histories
    ADD CONSTRAINT answers_to_histories_pkey PRIMARY KEY (id);


--
-- Name: answers_to_syndroms_pkey; Type: CONSTRAINT; Schema: public; Owner: pocketdoc; Tablespace: 
--

ALTER TABLE ONLY answers_to_syndroms
    ADD CONSTRAINT answers_to_syndroms_pkey PRIMARY KEY (id);


--
-- Name: diagnoses_pkey; Type: CONSTRAINT; Schema: public; Owner: pocketdoc; Tablespace: 
--

ALTER TABLE ONLY diagnoses
    ADD CONSTRAINT diagnoses_pkey PRIMARY KEY (id);


--
-- Name: diagnosis_descriptions_pkey; Type: CONSTRAINT; Schema: public; Owner: pocketdoc; Tablespace: 
--

ALTER TABLE ONLY diagnosis_descriptions
    ADD CONSTRAINT diagnosis_descriptions_pkey PRIMARY KEY (id);


--
-- Name: diagnosis_designations_pkey; Type: CONSTRAINT; Schema: public; Owner: pocketdoc; Tablespace: 
--

ALTER TABLE ONLY diagnosis_designations
    ADD CONSTRAINT diagnosis_designations_pkey PRIMARY KEY (id);


--
-- Name: followups_pkey; Type: CONSTRAINT; Schema: public; Owner: pocketdoc; Tablespace: 
--

ALTER TABLE ONLY followups
    ADD CONSTRAINT followups_pkey PRIMARY KEY (id);


--
-- Name: histories_pkey; Type: CONSTRAINT; Schema: public; Owner: pocketdoc; Tablespace: 
--

ALTER TABLE ONLY histories
    ADD CONSTRAINT histories_pkey PRIMARY KEY (id);


--
-- Name: languages_pkey; Type: CONSTRAINT; Schema: public; Owner: pocketdoc; Tablespace: 
--

ALTER TABLE ONLY languages
    ADD CONSTRAINT languages_pkey PRIMARY KEY (id);


--
-- Name: perfect_diagnosis_diagnoses_to_answers_pkey; Type: CONSTRAINT; Schema: public; Owner: pocketdoc; Tablespace: 
--

ALTER TABLE ONLY perfect_diagnosis_diagnoses_to_answers
    ADD CONSTRAINT perfect_diagnosis_diagnoses_to_answers_pkey PRIMARY KEY (id);


--
-- Name: question_descriptions_pkey; Type: CONSTRAINT; Schema: public; Owner: pocketdoc; Tablespace: 
--

ALTER TABLE ONLY question_descriptions
    ADD CONSTRAINT question_descriptions_pkey PRIMARY KEY (id);


--
-- Name: questions_pkey; Type: CONSTRAINT; Schema: public; Owner: pocketdoc; Tablespace: 
--

ALTER TABLE ONLY questions
    ADD CONSTRAINT questions_pkey PRIMARY KEY (id);


--
-- Name: score_distribution_answers_to_action_suggestions_pkey; Type: CONSTRAINT; Schema: public; Owner: pocketdoc; Tablespace: 
--

ALTER TABLE ONLY score_distribution_answers_to_action_suggestions
    ADD CONSTRAINT score_distribution_answers_to_action_suggestions_pkey PRIMARY KEY (id);


--
-- Name: score_distribution_answers_to_diagnoses_pkey; Type: CONSTRAINT; Schema: public; Owner: pocketdoc; Tablespace: 
--

ALTER TABLE ONLY score_distribution_answers_to_diagnoses
    ADD CONSTRAINT score_distribution_answers_to_diagnoses_pkey PRIMARY KEY (id);


--
-- Name: score_distribution_syndroms_to_action_suggestions_pkey; Type: CONSTRAINT; Schema: public; Owner: pocketdoc; Tablespace: 
--

ALTER TABLE ONLY score_distribution_syndroms_to_action_suggestions
    ADD CONSTRAINT score_distribution_syndroms_to_action_suggestions_pkey PRIMARY KEY (id);


--
-- Name: settings_pkey; Type: CONSTRAINT; Schema: public; Owner: pocketdoc; Tablespace: 
--

ALTER TABLE ONLY settings
    ADD CONSTRAINT settings_pkey PRIMARY KEY (id);


--
-- Name: syndroms_pkey; Type: CONSTRAINT; Schema: public; Owner: pocketdoc; Tablespace: 
--

ALTER TABLE ONLY syndroms
    ADD CONSTRAINT syndroms_pkey PRIMARY KEY (id);


--
-- Name: users_pkey; Type: CONSTRAINT; Schema: public; Owner: pocketdoc; Tablespace: 
--

ALTER TABLE ONLY users
    ADD CONSTRAINT users_pkey PRIMARY KEY (id);


--
-- Name: action_suggestion_answer_score; Type: INDEX; Schema: public; Owner: pocketdoc; Tablespace: 
--

CREATE INDEX action_suggestion_answer_score ON score_distribution_answers_to_action_suggestions USING btree (action_suggestion, answer);


--
-- Name: action_suggestion_score; Type: INDEX; Schema: public; Owner: pocketdoc; Tablespace: 
--

CREATE INDEX action_suggestion_score ON score_distribution_answers_to_action_suggestions USING btree (action_suggestion);


--
-- Name: action_suggestion_syndrom_score; Type: INDEX; Schema: public; Owner: pocketdoc; Tablespace: 
--

CREATE INDEX action_suggestion_syndrom_score ON score_distribution_syndroms_to_action_suggestions USING btree (action_suggestion);


--
-- Name: diagnosis_answer_score; Type: INDEX; Schema: public; Owner: pocketdoc; Tablespace: 
--

CREATE INDEX diagnosis_answer_score ON score_distribution_answers_to_diagnoses USING btree (diagnosis, answer);


--
-- Name: diagnosis_score; Type: INDEX; Schema: public; Owner: pocketdoc; Tablespace: 
--

CREATE INDEX diagnosis_score ON score_distribution_answers_to_diagnoses USING btree (diagnosis);


--
-- Name: syndrom_score; Type: INDEX; Schema: public; Owner: pocketdoc; Tablespace: 
--

CREATE INDEX syndrom_score ON score_distribution_syndroms_to_action_suggestions USING btree (syndrom);


--
-- Name: action_suggestion_descriptions_action_suggestion_fkey; Type: FK CONSTRAINT; Schema: public; Owner: pocketdoc
--

ALTER TABLE ONLY action_suggestion_descriptions
    ADD CONSTRAINT action_suggestion_descriptions_action_suggestion_fkey FOREIGN KEY (action_suggestion) REFERENCES action_suggestions(id) ON DELETE CASCADE;


--
-- Name: action_suggestion_descriptions_language_fkey; Type: FK CONSTRAINT; Schema: public; Owner: pocketdoc
--

ALTER TABLE ONLY action_suggestion_descriptions
    ADD CONSTRAINT action_suggestion_descriptions_language_fkey FOREIGN KEY (language) REFERENCES languages(id) ON DELETE CASCADE;


--
-- Name: action_suggestion_fkey; Type: FK CONSTRAINT; Schema: public; Owner: pocketdoc
--

ALTER TABLE ONLY followups
    ADD CONSTRAINT action_suggestion_fkey FOREIGN KEY (action_suggestion) REFERENCES action_suggestions(id) ON DELETE CASCADE;


--
-- Name: action_suggestion_fkey; Type: FK CONSTRAINT; Schema: public; Owner: pocketdoc
--

ALTER TABLE ONLY score_distribution_syndroms_to_action_suggestions
    ADD CONSTRAINT action_suggestion_fkey FOREIGN KEY (action_suggestion) REFERENCES action_suggestions(id) ON DELETE CASCADE;


--
-- Name: answers_to_histories_answer_fkey; Type: FK CONSTRAINT; Schema: public; Owner: pocketdoc
--

ALTER TABLE ONLY answers_to_histories
    ADD CONSTRAINT answers_to_histories_answer_fkey FOREIGN KEY (answer) REFERENCES answers(id) ON DELETE CASCADE;


--
-- Name: answers_to_histories_history_fkey; Type: FK CONSTRAINT; Schema: public; Owner: pocketdoc
--

ALTER TABLE ONLY answers_to_histories
    ADD CONSTRAINT answers_to_histories_history_fkey FOREIGN KEY (history) REFERENCES histories(id) ON DELETE CASCADE;


--
-- Name: answers_to_syndroms_answer_fkey; Type: FK CONSTRAINT; Schema: public; Owner: pocketdoc
--

ALTER TABLE ONLY answers_to_syndroms
    ADD CONSTRAINT answers_to_syndroms_answer_fkey FOREIGN KEY (answer) REFERENCES answers(id) ON DELETE CASCADE;


--
-- Name: answers_to_syndroms_syndrom_fkey; Type: FK CONSTRAINT; Schema: public; Owner: pocketdoc
--

ALTER TABLE ONLY answers_to_syndroms
    ADD CONSTRAINT answers_to_syndroms_syndrom_fkey FOREIGN KEY (syndrom) REFERENCES syndroms(id) ON DELETE CASCADE;


--
-- Name: diagnosis_descriptions_diagnosis_fkey; Type: FK CONSTRAINT; Schema: public; Owner: pocketdoc
--

ALTER TABLE ONLY diagnosis_descriptions
    ADD CONSTRAINT diagnosis_descriptions_diagnosis_fkey FOREIGN KEY (diagnosis) REFERENCES diagnoses(id) ON DELETE CASCADE;


--
-- Name: diagnosis_descriptions_language_fkey; Type: FK CONSTRAINT; Schema: public; Owner: pocketdoc
--

ALTER TABLE ONLY diagnosis_descriptions
    ADD CONSTRAINT diagnosis_descriptions_language_fkey FOREIGN KEY (language) REFERENCES languages(id) ON DELETE CASCADE;


--
-- Name: diagnosis_designations_diagnosis_fkey; Type: FK CONSTRAINT; Schema: public; Owner: pocketdoc
--

ALTER TABLE ONLY diagnosis_designations
    ADD CONSTRAINT diagnosis_designations_diagnosis_fkey FOREIGN KEY (diagnosis) REFERENCES diagnoses(id) ON DELETE CASCADE;


--
-- Name: diagnosis_designations_language_fkey; Type: FK CONSTRAINT; Schema: public; Owner: pocketdoc
--

ALTER TABLE ONLY diagnosis_designations
    ADD CONSTRAINT diagnosis_designations_language_fkey FOREIGN KEY (language) REFERENCES languages(id) ON DELETE CASCADE;


--
-- Name: diagnosis_fkey; Type: FK CONSTRAINT; Schema: public; Owner: pocketdoc
--

ALTER TABLE ONLY followups
    ADD CONSTRAINT diagnosis_fkey FOREIGN KEY (diagnosis) REFERENCES diagnoses(id) ON DELETE CASCADE;


--
-- Name: followup_fkey; Type: FK CONSTRAINT; Schema: public; Owner: pocketdoc
--

ALTER TABLE ONLY answers_to_histories
    ADD CONSTRAINT followup_fkey FOREIGN KEY (followup) REFERENCES followups(id) ON DELETE CASCADE;


--
-- Name: histories_last_answer_fkey; Type: FK CONSTRAINT; Schema: public; Owner: pocketdoc
--

ALTER TABLE ONLY histories
    ADD CONSTRAINT histories_last_answer_fkey FOREIGN KEY (last_answer) REFERENCES answers(id);


--
-- Name: perfect_diagnosis_diagnoses_to_answers_answer_fkey; Type: FK CONSTRAINT; Schema: public; Owner: pocketdoc
--

ALTER TABLE ONLY perfect_diagnosis_diagnoses_to_answers
    ADD CONSTRAINT perfect_diagnosis_diagnoses_to_answers_answer_fkey FOREIGN KEY (answer) REFERENCES answers(id) ON DELETE CASCADE;


--
-- Name: perfect_diagnosis_diagnoses_to_answers_diagnosis_fkey; Type: FK CONSTRAINT; Schema: public; Owner: pocketdoc
--

ALTER TABLE ONLY perfect_diagnosis_diagnoses_to_answers
    ADD CONSTRAINT perfect_diagnosis_diagnoses_to_answers_diagnosis_fkey FOREIGN KEY (diagnosis) REFERENCES diagnoses(id) ON DELETE CASCADE;


--
-- Name: question_descriptions_language_fkey; Type: FK CONSTRAINT; Schema: public; Owner: pocketdoc
--

ALTER TABLE ONLY question_descriptions
    ADD CONSTRAINT question_descriptions_language_fkey FOREIGN KEY (language) REFERENCES languages(id) ON DELETE CASCADE;


--
-- Name: question_descriptions_question_fkey; Type: FK CONSTRAINT; Schema: public; Owner: pocketdoc
--

ALTER TABLE ONLY question_descriptions
    ADD CONSTRAINT question_descriptions_question_fkey FOREIGN KEY (question) REFERENCES questions(id) ON DELETE CASCADE;


--
-- Name: question_fkey; Type: FK CONSTRAINT; Schema: public; Owner: pocketdoc
--

ALTER TABLE ONLY answers_to_histories
    ADD CONSTRAINT question_fkey FOREIGN KEY (question) REFERENCES questions(id) ON DELETE CASCADE;


--
-- Name: questions_answer_no_fkey; Type: FK CONSTRAINT; Schema: public; Owner: pocketdoc
--

ALTER TABLE ONLY questions
    ADD CONSTRAINT questions_answer_no_fkey FOREIGN KEY (answer_no) REFERENCES answers(id) ON DELETE CASCADE;


--
-- Name: questions_answer_yes_fkey; Type: FK CONSTRAINT; Schema: public; Owner: pocketdoc
--

ALTER TABLE ONLY questions
    ADD CONSTRAINT questions_answer_yes_fkey FOREIGN KEY (answer_yes) REFERENCES answers(id) ON DELETE CASCADE;


--
-- Name: questions_depends_on_fkey; Type: FK CONSTRAINT; Schema: public; Owner: pocketdoc
--

ALTER TABLE ONLY questions
    ADD CONSTRAINT questions_depends_on_fkey FOREIGN KEY (depends_on) REFERENCES answers(id);


--
-- Name: score_distribution_answers_to_action_sug_action_suggestion_fkey; Type: FK CONSTRAINT; Schema: public; Owner: pocketdoc
--

ALTER TABLE ONLY score_distribution_answers_to_action_suggestions
    ADD CONSTRAINT score_distribution_answers_to_action_sug_action_suggestion_fkey FOREIGN KEY (action_suggestion) REFERENCES action_suggestions(id) ON DELETE CASCADE;


--
-- Name: score_distribution_answers_to_action_suggestions_answer_fkey; Type: FK CONSTRAINT; Schema: public; Owner: pocketdoc
--

ALTER TABLE ONLY score_distribution_answers_to_action_suggestions
    ADD CONSTRAINT score_distribution_answers_to_action_suggestions_answer_fkey FOREIGN KEY (answer) REFERENCES answers(id) ON DELETE CASCADE;


--
-- Name: score_distribution_answers_to_diagnoses_answer_fkey; Type: FK CONSTRAINT; Schema: public; Owner: pocketdoc
--

ALTER TABLE ONLY score_distribution_answers_to_diagnoses
    ADD CONSTRAINT score_distribution_answers_to_diagnoses_answer_fkey FOREIGN KEY (answer) REFERENCES answers(id) ON DELETE CASCADE;


--
-- Name: score_distribution_answers_to_diagnoses_diagnosis_fkey; Type: FK CONSTRAINT; Schema: public; Owner: pocketdoc
--

ALTER TABLE ONLY score_distribution_answers_to_diagnoses
    ADD CONSTRAINT score_distribution_answers_to_diagnoses_diagnosis_fkey FOREIGN KEY (diagnosis) REFERENCES diagnoses(id) ON DELETE CASCADE;


--
-- Name: syndrom_fkey; Type: FK CONSTRAINT; Schema: public; Owner: pocketdoc
--

ALTER TABLE ONLY score_distribution_syndroms_to_action_suggestions
    ADD CONSTRAINT syndrom_fkey FOREIGN KEY (syndrom) REFERENCES syndroms(id) ON DELETE CASCADE;


--
-- Name: user_fkey; Type: FK CONSTRAINT; Schema: public; Owner: pocketdoc
--

ALTER TABLE ONLY followups
    ADD CONSTRAINT user_fkey FOREIGN KEY (usr) REFERENCES users(id) ON DELETE CASCADE;


--
-- Name: users_history_fkey; Type: FK CONSTRAINT; Schema: public; Owner: pocketdoc
--

ALTER TABLE ONLY users
    ADD CONSTRAINT users_history_fkey FOREIGN KEY (history) REFERENCES histories(id);


--
-- Name: public; Type: ACL; Schema: -; Owner: postgres
--

REVOKE ALL ON SCHEMA public FROM PUBLIC;
REVOKE ALL ON SCHEMA public FROM postgres;
GRANT ALL ON SCHEMA public TO postgres;
GRANT ALL ON SCHEMA public TO PUBLIC;


--
-- PostgreSQL database dump complete
--

