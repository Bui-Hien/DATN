import React, {useEffect, useMemo, useState} from 'react';
import {Calendar, momentLocalizer, Views} from 'react-big-calendar';
import moment from 'moment';
import 'react-big-calendar/lib/css/react-big-calendar.css';
import {Button, ButtonGroup} from "@mui/material";
import AccessTimeIcon from "@mui/icons-material/AccessTime";
import {t} from "i18next";
import {useStore} from "../../stores";
import TimeSheetDetailForm from "../TimeSheetDetail/TimeSheetDetailForm";

const localizer = momentLocalizer(moment);

const WorkScheduleCalendar = () => {
    const [currentDate, setCurrentDate] = useState(new Date());
    const {staffWorkScheduleStore, staffStore} = useStore();
    // Sample data from your JSON
    const scheduleData = {
        "staff": {
            "id": "c18b7327-ed53-4697-87a3-06b406f6c4d1",
            "displayName": "BÙI HIỀN",
            "staffCode": "NV25050001"
        },
        "staffWorkSchedules": {
            "2025-05-08T17:00:00.000+00:00": [
                {
                    "id": "dd09058b-3286-4a64-aee1-2b6a01c2d110",
                    "voided": false,
                    "createdAt": "2025-05-24T21:13:03.285269",
                    "updatedAt": "2025-05-24T22:23:08.771447",
                    "createdBy": "admin",
                    "updatedBy": "admin",
                    "shiftWorkType": 3,
                    "workingDate": "2025-05-08T17:00:00.000+00:00",
                    "shiftWorkStatus": 1
                },
                {
                    "id": "f102761a-f0c6-4c48-89b4-4a14b1b3a293",
                    "voided": false,
                    "createdAt": "2025-05-24T23:43:13.710361",
                    "updatedAt": "2025-05-24T23:43:13.710361",
                    "createdBy": "admin",
                    "updatedBy": "admin",
                    "shiftWorkType": 2,
                    "workingDate": "2025-05-08T17:00:00.000+00:00",
                    "shiftWorkStatus": 1
                }
            ],
            "2025-05-13T17:00:00.000+00:00": [
                {
                    "id": "c20cecb4-5d83-43a2-bd9a-b6098193eba7",
                    "voided": false,
                    "createdAt": "2025-05-24T23:43:13.710361",
                    "updatedAt": "2025-05-24T23:43:13.710361",
                    "createdBy": "admin",
                    "updatedBy": "admin",
                    "shiftWorkType": 2,
                    "workingDate": "2025-05-13T17:00:00.000+00:00",
                    "shiftWorkStatus": 1
                },
                {
                    "id": "d7ee7aad-2f43-4fd4-b4be-3874c1c44659",
                    "voided": false,
                    "createdAt": "2025-05-24T21:13:03.301858",
                    "updatedAt": "2025-05-24T22:23:08.787448",
                    "createdBy": "admin",
                    "updatedBy": "admin",
                    "shiftWorkType": 3,
                    "workingDate": "2025-05-13T17:00:00.000+00:00",
                    "shiftWorkStatus": 1
                }
            ],
            "2025-05-18T17:00:00.000+00:00": [
                {
                    "id": "4ce9fe93-bfc0-4c60-8b30-6509f5e56c5b",
                    "voided": false,
                    "createdAt": "2025-05-24T21:13:03.301858",
                    "updatedAt": "2025-05-24T22:23:08.802447",
                    "createdBy": "admin",
                    "updatedBy": "admin",
                    "shiftWorkType": 3,
                    "workingDate": "2025-05-18T17:00:00.000+00:00",
                    "shiftWorkStatus": 1
                },
                {
                    "id": "9aaacb4d-2efc-4304-9df9-cdc93f0138c1",
                    "voided": false,
                    "createdAt": "2025-05-24T23:43:13.710361",
                    "updatedAt": "2025-05-24T23:43:13.710361",
                    "createdBy": "admin",
                    "updatedBy": "admin",
                    "shiftWorkType": 2,
                    "workingDate": "2025-05-18T17:00:00.000+00:00",
                    "shiftWorkStatus": 1
                }
            ],
            "2025-05-23T17:00:00.000+00:00": [
                {
                    "id": "2fc74064-5e91-4f41-bec0-f19e7000e571",
                    "voided": false,
                    "createdAt": "2025-05-24T23:43:13.725114",
                    "updatedAt": "2025-05-24T23:43:13.725114",
                    "createdBy": "admin",
                    "updatedBy": "admin",
                    "shiftWorkType": 2,
                    "workingDate": "2025-05-23T17:00:00.000+00:00",
                    "shiftWorkStatus": 1
                },
                {
                    "id": "92e54ae0-d19b-4991-b089-7cbf181f0a33",
                    "voided": false,
                    "createdAt": "2025-05-24T21:13:03.307368",
                    "updatedAt": "2025-05-24T23:33:03.790114",
                    "createdBy": "admin",
                    "updatedBy": "admin",
                    "shiftWorkType": 3,
                    "workingDate": "2025-05-23T17:00:00.000+00:00",
                    "checkIn": "2025-05-24T00:00:03.325+00:00",
                    "checkOut": "2025-05-24T11:00:00.000+00:00",
                    "shiftWorkStatus": 4
                }
            ],
            "2025-05-28T17:00:00.000+00:00": [
                {
                    "id": "c69cba5b-a12c-4247-b876-df4a787678b3",
                    "voided": false,
                    "createdAt": "2025-05-24T21:13:03.307368",
                    "updatedAt": "2025-05-24T22:23:08.83346",
                    "createdBy": "admin",
                    "updatedBy": "admin",
                    "shiftWorkType": 3,
                    "workingDate": "2025-05-28T17:00:00.000+00:00",
                    "shiftWorkStatus": 1
                },
                {
                    "id": "e298f6d4-d686-4b33-b05c-489033bb9afb",
                    "voided": false,
                    "createdAt": "2025-05-24T23:43:13.727229",
                    "updatedAt": "2025-05-24T23:43:13.727229",
                    "createdBy": "admin",
                    "updatedBy": "admin",
                    "shiftWorkType": 2,
                    "workingDate": "2025-05-28T17:00:00.000+00:00",
                    "shiftWorkStatus": 1
                }
            ],
            "2025-05-01T17:00:00.000+00:00": [
                {
                    "id": "af1f5f28-cc2e-4571-a243-ff2dc35acc4a",
                    "voided": false,
                    "createdAt": "2025-05-24T21:13:03.285269",
                    "updatedAt": "2025-05-24T22:23:08.745447",
                    "createdBy": "admin",
                    "updatedBy": "admin",
                    "shiftWorkType": 3,
                    "workingDate": "2025-05-01T17:00:00.000+00:00",
                    "shiftWorkStatus": 1
                },
                {
                    "id": "50128844-6662-452c-bbed-73bb6a51df44",
                    "voided": false,
                    "createdAt": "2025-05-24T23:43:13.709466",
                    "updatedAt": "2025-05-24T23:43:13.709466",
                    "createdBy": "admin",
                    "updatedBy": "admin",
                    "shiftWorkType": 2,
                    "workingDate": "2025-05-01T17:00:00.000+00:00",
                    "shiftWorkStatus": 1
                }
            ],
            "2025-05-06T17:00:00.000+00:00": [
                {
                    "id": "57ec3b35-d0c6-49a3-960b-e0e85cc3d879",
                    "voided": false,
                    "createdAt": "2025-05-24T21:13:03.285269",
                    "updatedAt": "2025-05-24T22:23:08.762448",
                    "createdBy": "admin",
                    "updatedBy": "admin",
                    "shiftWorkType": 3,
                    "workingDate": "2025-05-06T17:00:00.000+00:00",
                    "shiftWorkStatus": 1
                },
                {
                    "id": "5534de9f-4bba-4eff-9f86-bbab807f6078",
                    "voided": false,
                    "createdAt": "2025-05-24T23:43:13.710361",
                    "updatedAt": "2025-05-24T23:43:13.710361",
                    "createdBy": "admin",
                    "updatedBy": "admin",
                    "shiftWorkType": 2,
                    "workingDate": "2025-05-06T17:00:00.000+00:00",
                    "shiftWorkStatus": 1
                }
            ],
            "2025-05-11T17:00:00.000+00:00": [
                {
                    "id": "11d98119-b1d8-4713-97e2-e94deff3f792",
                    "voided": false,
                    "createdAt": "2025-05-24T21:13:03.285269",
                    "updatedAt": "2025-05-24T22:23:08.779447",
                    "createdBy": "admin",
                    "updatedBy": "admin",
                    "shiftWorkType": 3,
                    "workingDate": "2025-05-11T17:00:00.000+00:00",
                    "shiftWorkStatus": 1
                },
                {
                    "id": "515190b4-8266-468e-8c47-2ae2e79846d3",
                    "voided": false,
                    "createdAt": "2025-05-24T23:43:13.710361",
                    "updatedAt": "2025-05-24T23:43:13.710361",
                    "createdBy": "admin",
                    "updatedBy": "admin",
                    "shiftWorkType": 2,
                    "workingDate": "2025-05-11T17:00:00.000+00:00",
                    "shiftWorkStatus": 1
                }
            ],
            "2025-05-26T17:00:00.000+00:00": [
                {
                    "id": "4bf48b32-7d2b-478d-ade6-e96f63ad82a2",
                    "voided": false,
                    "createdAt": "2025-05-24T23:43:13.727229",
                    "updatedAt": "2025-05-24T23:43:13.727229",
                    "createdBy": "admin",
                    "updatedBy": "admin",
                    "shiftWorkType": 2,
                    "workingDate": "2025-05-26T17:00:00.000+00:00",
                    "shiftWorkStatus": 1
                },
                {
                    "id": "ec802735-b764-4d16-b005-54cbc941d27c",
                    "voided": false,
                    "createdAt": "2025-05-24T21:13:03.307368",
                    "updatedAt": "2025-05-24T22:23:08.82545",
                    "createdBy": "admin",
                    "updatedBy": "admin",
                    "shiftWorkType": 3,
                    "workingDate": "2025-05-26T17:00:00.000+00:00",
                    "shiftWorkStatus": 1
                }
            ],
            "2025-05-16T17:00:00.000+00:00": [
                {
                    "id": "325c9d13-7e3f-4e42-a32b-f6f1819684c5",
                    "voided": false,
                    "createdAt": "2025-05-24T21:13:03.301858",
                    "updatedAt": "2025-05-24T22:23:08.799448",
                    "createdBy": "admin",
                    "updatedBy": "admin",
                    "shiftWorkType": 3,
                    "workingDate": "2025-05-16T17:00:00.000+00:00",
                    "shiftWorkStatus": 1
                },
                {
                    "id": "d169a2e7-ba10-44e1-b0ea-07553695c93d",
                    "voided": false,
                    "createdAt": "2025-05-24T23:43:13.710361",
                    "updatedAt": "2025-05-24T23:43:13.710361",
                    "createdBy": "admin",
                    "updatedBy": "admin",
                    "shiftWorkType": 2,
                    "workingDate": "2025-05-16T17:00:00.000+00:00",
                    "shiftWorkStatus": 1
                }
            ],
            "2025-05-21T17:00:00.000+00:00": [
                {
                    "id": "e693a8b1-e955-4dc8-ac34-02017c1a338b",
                    "voided": false,
                    "createdAt": "2025-05-24T21:13:03.301858",
                    "updatedAt": "2025-05-24T22:23:08.811447",
                    "createdBy": "admin",
                    "updatedBy": "admin",
                    "shiftWorkType": 3,
                    "workingDate": "2025-05-21T17:00:00.000+00:00",
                    "shiftWorkStatus": 1
                },
                {
                    "id": "80e1d44e-842c-4caf-99bf-3b466898ee3c",
                    "voided": false,
                    "createdAt": "2025-05-24T23:43:13.710361",
                    "updatedAt": "2025-05-24T23:43:13.710361",
                    "createdBy": "admin",
                    "updatedBy": "admin",
                    "shiftWorkType": 2,
                    "workingDate": "2025-05-21T17:00:00.000+00:00",
                    "shiftWorkStatus": 1
                }
            ],
            "2025-05-02T17:00:00.000+00:00": [
                {
                    "id": "416ab396-6dc9-43bb-8477-23d353a658f8",
                    "voided": false,
                    "createdAt": "2025-05-24T21:13:03.285269",
                    "updatedAt": "2025-05-24T22:23:08.751447",
                    "createdBy": "admin",
                    "updatedBy": "admin",
                    "shiftWorkType": 3,
                    "workingDate": "2025-05-02T17:00:00.000+00:00",
                    "shiftWorkStatus": 1
                },
                {
                    "id": "b8440f60-f4ca-42a5-9138-f96129699a68",
                    "voided": false,
                    "createdAt": "2025-05-24T23:43:13.710361",
                    "updatedAt": "2025-05-24T23:43:13.710361",
                    "createdBy": "admin",
                    "updatedBy": "admin",
                    "shiftWorkType": 2,
                    "workingDate": "2025-05-02T17:00:00.000+00:00",
                    "shiftWorkStatus": 1
                }
            ],
            "2025-05-07T17:00:00.000+00:00": [
                {
                    "id": "ee589d52-afff-4e92-bd51-88f0486d9c2a",
                    "voided": false,
                    "createdAt": "2025-05-24T23:43:13.710361",
                    "updatedAt": "2025-05-24T23:43:13.710361",
                    "createdBy": "admin",
                    "updatedBy": "admin",
                    "shiftWorkType": 2,
                    "workingDate": "2025-05-07T17:00:00.000+00:00",
                    "shiftWorkStatus": 1
                },
                {
                    "id": "5bc36727-11c1-4ed1-8d57-2eb4f2973177",
                    "voided": false,
                    "createdAt": "2025-05-24T21:13:03.285269",
                    "updatedAt": "2025-05-24T22:23:08.768448",
                    "createdBy": "admin",
                    "updatedBy": "admin",
                    "shiftWorkType": 3,
                    "workingDate": "2025-05-07T17:00:00.000+00:00",
                    "shiftWorkStatus": 1
                }
            ],
            "2025-05-12T17:00:00.000+00:00": [
                {
                    "id": "9d214fd0-e936-4c39-a4a8-9297072e9216",
                    "voided": false,
                    "createdAt": "2025-05-24T21:13:03.300793",
                    "updatedAt": "2025-05-24T22:23:08.784447",
                    "createdBy": "admin",
                    "updatedBy": "admin",
                    "shiftWorkType": 3,
                    "workingDate": "2025-05-12T17:00:00.000+00:00",
                    "shiftWorkStatus": 1
                },
                {
                    "id": "05ddab6c-3a8a-4908-b431-44e2c63e4865",
                    "voided": false,
                    "createdAt": "2025-05-24T23:43:13.710361",
                    "updatedAt": "2025-05-24T23:43:13.710361",
                    "createdBy": "admin",
                    "updatedBy": "admin",
                    "shiftWorkType": 2,
                    "workingDate": "2025-05-12T17:00:00.000+00:00",
                    "shiftWorkStatus": 1
                }
            ],
            "2025-05-19T17:00:00.000+00:00": [
                {
                    "id": "7a4b7baa-3fc3-4102-a04d-9dbeb5adf0be",
                    "voided": false,
                    "createdAt": "2025-05-24T21:13:03.301858",
                    "updatedAt": "2025-05-24T22:23:08.805448",
                    "createdBy": "admin",
                    "updatedBy": "admin",
                    "shiftWorkType": 3,
                    "workingDate": "2025-05-19T17:00:00.000+00:00",
                    "shiftWorkStatus": 1
                },
                {
                    "id": "66f13f79-5012-45f7-89d8-5e4b6a2ab7be",
                    "voided": false,
                    "createdAt": "2025-05-24T23:43:13.710361",
                    "updatedAt": "2025-05-24T23:43:13.710361",
                    "createdBy": "admin",
                    "updatedBy": "admin",
                    "shiftWorkType": 2,
                    "workingDate": "2025-05-19T17:00:00.000+00:00",
                    "shiftWorkStatus": 1
                }
            ],
            "2025-05-04T17:00:00.000+00:00": [
                {
                    "id": "c1c4312e-d8ff-4f34-b75b-6b058bc51274",
                    "voided": false,
                    "createdAt": "2025-05-24T23:43:13.710361",
                    "updatedAt": "2025-05-24T23:43:13.710361",
                    "createdBy": "admin",
                    "updatedBy": "admin",
                    "shiftWorkType": 2,
                    "workingDate": "2025-05-04T17:00:00.000+00:00",
                    "shiftWorkStatus": 1
                },
                {
                    "id": "2fbf7906-03bd-465e-8316-876a28b39beb",
                    "voided": false,
                    "createdAt": "2025-05-24T21:13:03.285269",
                    "updatedAt": "2025-05-24T22:23:08.754446",
                    "createdBy": "admin",
                    "updatedBy": "admin",
                    "shiftWorkType": 3,
                    "workingDate": "2025-05-04T17:00:00.000+00:00",
                    "shiftWorkStatus": 1
                }
            ],
            "2025-05-09T17:00:00.000+00:00": [
                {
                    "id": "374dadb1-80f0-4404-b8d2-b325a9f6ed68",
                    "voided": false,
                    "createdAt": "2025-05-24T21:13:03.285269",
                    "updatedAt": "2025-05-24T22:23:08.775447",
                    "createdBy": "admin",
                    "updatedBy": "admin",
                    "shiftWorkType": 3,
                    "workingDate": "2025-05-09T17:00:00.000+00:00",
                    "shiftWorkStatus": 1
                },
                {
                    "id": "65089741-6588-4912-891d-ab08268e6a98",
                    "voided": false,
                    "createdAt": "2025-05-24T23:43:13.710361",
                    "updatedAt": "2025-05-24T23:43:13.710361",
                    "createdBy": "admin",
                    "updatedBy": "admin",
                    "shiftWorkType": 2,
                    "workingDate": "2025-05-09T17:00:00.000+00:00",
                    "shiftWorkStatus": 1
                }
            ],
            "2025-05-14T17:00:00.000+00:00": [
                {
                    "id": "d003ba4b-3a69-4185-b2af-91df50e30682",
                    "voided": false,
                    "createdAt": "2025-05-24T23:43:13.710361",
                    "updatedAt": "2025-05-24T23:43:13.710361",
                    "createdBy": "admin",
                    "updatedBy": "admin",
                    "shiftWorkType": 2,
                    "workingDate": "2025-05-14T17:00:00.000+00:00",
                    "shiftWorkStatus": 1
                },
                {
                    "id": "6b516d62-fe74-4f9c-a53d-b2efae4752ea",
                    "voided": false,
                    "createdAt": "2025-05-24T21:13:03.301858",
                    "updatedAt": "2025-05-24T22:23:08.790448",
                    "createdBy": "admin",
                    "updatedBy": "admin",
                    "shiftWorkType": 3,
                    "workingDate": "2025-05-14T17:00:00.000+00:00",
                    "shiftWorkStatus": 1
                }
            ],
            "2025-05-29T17:00:00.000+00:00": [
                {
                    "id": "2cbf37e5-2bb2-429b-81a4-cdd191ad4f40",
                    "voided": false,
                    "createdAt": "2025-05-24T23:43:13.727229",
                    "updatedAt": "2025-05-24T23:43:13.727229",
                    "createdBy": "admin",
                    "updatedBy": "admin",
                    "shiftWorkType": 2,
                    "workingDate": "2025-05-29T17:00:00.000+00:00",
                    "shiftWorkStatus": 1
                },
                {
                    "id": "f9e520cd-9efb-4ecb-8a88-ca13d9f754aa",
                    "voided": false,
                    "createdAt": "2025-05-24T21:13:03.307368",
                    "updatedAt": "2025-05-24T22:23:08.836448",
                    "createdBy": "admin",
                    "updatedBy": "admin",
                    "shiftWorkType": 3,
                    "workingDate": "2025-05-29T17:00:00.000+00:00",
                    "shiftWorkStatus": 1
                }
            ],
            "2025-04-30T17:00:00.000+00:00": [
                {
                    "id": "31b6453b-d284-49b9-9c2a-bbb82e0132ce",
                    "voided": false,
                    "createdAt": "2025-05-24T21:13:03.285269",
                    "updatedAt": "2025-05-24T23:39:05.02713",
                    "createdBy": "admin",
                    "updatedBy": "admin",
                    "shiftWorkType": 3,
                    "workingDate": "2025-04-30T17:00:00.000+00:00",
                    "checkIn": "2025-05-24T00:00:39.833+00:00",
                    "checkOut": "2025-05-24T11:00:00.000+00:00",
                    "shiftWorkStatus": 4
                },
                {
                    "id": "bba2a996-dfbe-47d9-ba22-74af4ff7f49a",
                    "voided": false,
                    "createdAt": "2025-05-24T23:43:13.627709",
                    "updatedAt": "2025-05-24T23:45:07.438615",
                    "createdBy": "admin",
                    "updatedBy": "admin",
                    "shiftWorkType": 2,
                    "workingDate": "2025-04-30T17:00:00.000+00:00",
                    "checkIn": "2025-05-01T00:00:22.982+00:00",
                    "checkOut": "2025-05-01T11:00:00.000+00:00",
                    "shiftWorkStatus": 3
                }
            ],
            "2025-05-25T17:00:00.000+00:00": [
                {
                    "id": "06e3848d-b01b-47d2-8b04-9e3fb5f305ec",
                    "voided": false,
                    "createdAt": "2025-05-24T23:43:13.727229",
                    "updatedAt": "2025-05-24T23:43:13.727229",
                    "createdBy": "admin",
                    "updatedBy": "admin",
                    "shiftWorkType": 2,
                    "workingDate": "2025-05-25T17:00:00.000+00:00",
                    "shiftWorkStatus": 1
                },
                {
                    "id": "99a3dee4-c013-44d1-bf9b-b9deb1250a4f",
                    "voided": false,
                    "createdAt": "2025-05-24T21:13:03.307368",
                    "updatedAt": "2025-05-24T22:23:08.821446",
                    "createdBy": "admin",
                    "updatedBy": "admin",
                    "shiftWorkType": 3,
                    "workingDate": "2025-05-25T17:00:00.000+00:00",
                    "shiftWorkStatus": 1
                }
            ],
            "2025-05-05T17:00:00.000+00:00": [
                {
                    "id": "a0acef86-fd53-48c1-9d2d-8b24c855b637",
                    "voided": false,
                    "createdAt": "2025-05-24T21:13:03.285269",
                    "updatedAt": "2025-05-24T22:23:08.758448",
                    "createdBy": "admin",
                    "updatedBy": "admin",
                    "shiftWorkType": 3,
                    "workingDate": "2025-05-05T17:00:00.000+00:00",
                    "shiftWorkStatus": 1
                },
                {
                    "id": "9e277e9f-2dbf-4239-a6b3-2070d1288369",
                    "voided": false,
                    "createdAt": "2025-05-24T23:43:13.710361",
                    "updatedAt": "2025-05-24T23:43:13.710361",
                    "createdBy": "admin",
                    "updatedBy": "admin",
                    "shiftWorkType": 2,
                    "workingDate": "2025-05-05T17:00:00.000+00:00",
                    "shiftWorkStatus": 1
                }
            ],
            "2025-05-15T17:00:00.000+00:00": [
                {
                    "id": "839a06a6-58f7-4da5-a546-28739b502a97",
                    "voided": false,
                    "createdAt": "2025-05-24T21:13:03.301858",
                    "updatedAt": "2025-05-24T22:23:08.794447",
                    "createdBy": "admin",
                    "updatedBy": "admin",
                    "shiftWorkType": 3,
                    "workingDate": "2025-05-15T17:00:00.000+00:00",
                    "shiftWorkStatus": 1
                },
                {
                    "id": "80465211-4f83-4863-8789-65077195313b",
                    "voided": false,
                    "createdAt": "2025-05-24T23:43:13.710361",
                    "updatedAt": "2025-05-24T23:43:13.710361",
                    "createdBy": "admin",
                    "updatedBy": "admin",
                    "shiftWorkType": 2,
                    "workingDate": "2025-05-15T17:00:00.000+00:00",
                    "shiftWorkStatus": 1
                }
            ],
            "2025-05-20T17:00:00.000+00:00": [
                {
                    "id": "cd2ed298-cc44-4297-9b4d-e594edc8509b",
                    "voided": false,
                    "createdAt": "2025-05-24T21:13:03.301858",
                    "updatedAt": "2025-05-24T22:23:08.808447",
                    "createdBy": "admin",
                    "updatedBy": "admin",
                    "shiftWorkType": 3,
                    "workingDate": "2025-05-20T17:00:00.000+00:00",
                    "shiftWorkStatus": 1
                },
                {
                    "id": "0f9507f7-6ac5-411e-b02c-2837f405bf86",
                    "voided": false,
                    "createdAt": "2025-05-24T23:43:13.710361",
                    "updatedAt": "2025-05-24T23:43:13.710361",
                    "createdBy": "admin",
                    "updatedBy": "admin",
                    "shiftWorkType": 2,
                    "workingDate": "2025-05-20T17:00:00.000+00:00",
                    "shiftWorkStatus": 1
                }
            ],
            "2025-05-30T17:00:00.000+00:00": [
                {
                    "id": "74094d42-dcb2-4247-97d2-3ff27dad2591",
                    "voided": false,
                    "createdAt": "2025-05-24T23:43:13.727229",
                    "updatedAt": "2025-05-24T23:43:13.727229",
                    "createdBy": "admin",
                    "updatedBy": "admin",
                    "shiftWorkType": 2,
                    "workingDate": "2025-05-30T17:00:00.000+00:00",
                    "shiftWorkStatus": 1
                },
                {
                    "id": "f1d7ecfc-6ffb-42fa-9b46-b949db0f49b4",
                    "voided": false,
                    "createdAt": "2025-05-24T21:13:03.307368",
                    "updatedAt": "2025-05-24T22:23:08.842447",
                    "createdBy": "admin",
                    "updatedBy": "admin",
                    "shiftWorkType": 3,
                    "workingDate": "2025-05-30T17:00:00.000+00:00",
                    "shiftWorkStatus": 1
                }
            ],
            "2025-05-27T17:00:00.000+00:00": [
                {
                    "id": "dda8d7ed-30ed-4f8c-a7a3-cb587849eff0",
                    "voided": false,
                    "createdAt": "2025-05-24T21:13:03.307368",
                    "updatedAt": "2025-05-24T22:23:08.828448",
                    "createdBy": "admin",
                    "updatedBy": "admin",
                    "shiftWorkType": 3,
                    "workingDate": "2025-05-27T17:00:00.000+00:00",
                    "shiftWorkStatus": 1
                },
                {
                    "id": "69d6332d-ad3c-40e6-a8ea-ef417e679280",
                    "voided": false,
                    "createdAt": "2025-05-24T23:43:13.727229",
                    "updatedAt": "2025-05-24T23:43:13.727229",
                    "createdBy": "admin",
                    "updatedBy": "admin",
                    "shiftWorkType": 2,
                    "workingDate": "2025-05-27T17:00:00.000+00:00",
                    "shiftWorkStatus": 1
                }
            ],
            "2025-05-22T17:00:00.000+00:00": [
                {
                    "id": "b9fcc205-a7dc-4f51-831d-165c075f93a1",
                    "voided": false,
                    "createdAt": "2025-05-24T21:13:03.307368",
                    "updatedAt": "2025-05-24T22:23:08.815449",
                    "createdBy": "admin",
                    "updatedBy": "admin",
                    "shiftWorkType": 3,
                    "workingDate": "2025-05-22T17:00:00.000+00:00",
                    "shiftWorkStatus": 1
                },
                {
                    "id": "00e42efb-d108-4800-b5d5-20924f42803c",
                    "voided": false,
                    "createdAt": "2025-05-24T23:43:13.710361",
                    "updatedAt": "2025-05-24T23:43:13.710361",
                    "createdBy": "admin",
                    "updatedBy": "admin",
                    "shiftWorkType": 2,
                    "workingDate": "2025-05-22T17:00:00.000+00:00",
                    "shiftWorkStatus": 1
                }
            ]
        }
    };

    // Transform schedule data to calendar events
    const events = useMemo(() => {
        const eventList = [];

        Object.entries(scheduleData.staffWorkSchedules).forEach(([dateKey, shifts]) => {
            const workDate = new Date(dateKey.split('T')[0]); // Get date part only

            shifts.forEach((shift, index) => {
                // Create event for each shift
                eventList.push({
                    id: shift.id,
                    start: new Date(workDate.getFullYear(), workDate.getMonth(), workDate.getDate(), 8 + (index * 4), 0), // Stagger times for display
                    end: new Date(workDate.getFullYear(), workDate.getMonth(), workDate.getDate(), 12 + (index * 4), 0),
                    resource: {
                        statusName: "statusInfo.name",
                        shift: shift
                    }
                });
            });
        });

        return eventList;
    }, []);

    // Custom event component
    const EventComponent = ({event}) => {
        return (
            <div
                style={{
                    backgroundColor: event.resource.bgColor,
                    border: `2px solid ${event.resource.color}`,
                    borderRadius: '4px',
                    padding: '2px 4px',
                    fontSize: '12px',
                    fontWeight: 'bold',
                    color: '#333',
                    maxHeight: '100px',
                    overflowY: 'auto'
                }}
            >
                {event.resource.shift.workingDate}
                <br/>
                {event.resource.shift.workingDate}
                <br/>
                <br/>
                <br/>
                {event.resource.shift.workingDate}
                {event.resource.shift.workingDate}
                <br/>
                <br/>
                {event.resource.shift.workingDate}
                <br/>
                {event.resource.shift.workingDate}
                <br/>
                {event.resource.shift.workingDate}
                <br/>
                {event.resource.shift.workingDate}
                <br/>
                {event.resource.shift.workingDate}
                <br/>
                {event.resource.shift.workingDate}
                <br/>
                {event.resource.shift.workingDate}
                <br/>
                {event.resource.shift.workingDate}
            </div>
        );
    };

    // Navigation handlers
    const handleNavigate = (date) => {
        setCurrentDate(date);
    };

    const handleToday = () => {
        setCurrentDate(new Date());
    };

    const handleNext = () => {
        const nextMonth = new Date(currentDate);
        nextMonth.setMonth(nextMonth.getMonth() + 1);
        setCurrentDate(nextMonth);
    };

    const handleBack = () => {
        const prevMonth = new Date(currentDate);
        prevMonth.setMonth(prevMonth.getMonth() - 1);
        setCurrentDate(prevMonth);
    };
    const {openCreateEditPopup, handleOpenCreateEdit} = staffWorkScheduleStore;

    return (
        <div className="p-6 bg-white">
            <div className="grid grid-cols-12 gap-4">
                <div className="col-span-4">
                    <h1 className="text-2xl font-bold text-gray-800 mb-2">
                        Lịch Làm Việc - {scheduleData.staff.displayName}
                    </h1>
                    <p className="text-gray-600">Mã NV: {scheduleData.staff.staffCode}</p>
                </div>
                <div className="col-span-4 flex justify-center items-center">
                    <ButtonGroup
                        color="container"
                        aria-label="outlined primary button group"
                    >
                        <Button
                            onClick={() => {
                                handleOpenCreateEdit(null)
                            }}
                            startIcon={<AccessTimeIcon/>}
                        >
                            {t("Chấm công")}
                        </Button>
                    </ButtonGroup>
                </div>
            </div>

            {/* Custom Navigation */}
            <div className="mb-4 flex justify-between items-center">
                <div className="flex gap-2">
                    <button
                        onClick={handleToday}
                        className="px-4 py-2 bg-blue-500 text-white rounded hover:bg-blue-600 transition-colors"
                    >
                        Hôm nay
                    </button>
                    <button
                        onClick={handleBack}
                        className="px-4 py-2 bg-gray-500 text-white rounded hover:bg-gray-600 transition-colors"
                    >
                        ←
                    </button>
                    <button
                        onClick={handleNext}
                        className="px-4 py-2 bg-gray-500 text-white rounded hover:bg-gray-600 transition-colors"
                    >
                        →
                    </button>
                </div>
            </div>

            {/* Calendar */}
            <div style={{height: '600px'}}>
                <Calendar
                    localizer={localizer}
                    events={events}
                    startAccessor="start"
                    endAccessor="end"
                    views={[Views.MONTH]}
                    defaultView={Views.MONTH}
                    view={Views.MONTH}
                    date={currentDate}
                    onNavigate={handleNavigate}
                    components={{
                        event: EventComponent
                    }}
                    eventPropGetter={(event) => ({
                        style: {
                            backgroundColor: 'transparent',
                            border: 'none',
                            borderRadius: '4px',
                        }
                    })}
                    dayPropGetter={(date) => {
                        const today = new Date();
                        const isToday = date.toDateString() === today.toDateString();
                        return {
                            style: {
                                backgroundColor: isToday ? '#e3f2fd' : 'transparent'
                            }
                        };
                    }}
                    toolbar={false} // Disable default toolbar since we have custom navigation
                    popup
                    showMultiDayTimes
                />
            </div>
            {openCreateEditPopup && <TimeSheetDetailForm isPerson={true}/>}
        </div>
    );
};

export default WorkScheduleCalendar;