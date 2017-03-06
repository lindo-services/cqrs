CREATE TABLE TestEvent
(
  id uuid NOT NULL,
  aggregateId uuid NOT NULL,
  aggregateType text NOT NULL,
  eventType text NOT NULL,
  aggregateSequenceNumber bigint NOT NULL,
  feedSequenceNumber bigint NOT NULL,
  occured timestamp  NOT NULL,
  data jsonb,

  CONSTRAINT testevent_pkey PRIMARY KEY (aggregateId, aggregateSequenceNumber)
);