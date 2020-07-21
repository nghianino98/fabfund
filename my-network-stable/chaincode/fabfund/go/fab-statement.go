
 package main

 import (
	 "bytes"
	 "encoding/json"
	 "fmt"
	 "github.com/hyperledger/fabric-chaincode-go/shim"

	 sc "github.com/hyperledger/fabric-protos-go/peer"
	 "strconv"
 )

 // Define the Smart Contract structure
 type StatementSmartContract struct {
 }

 // Define the  Statement
 type Statement struct {
	 Title                string `json:"title"`
	 Description	      string `json:"description"`
	 Quarter              string `json:"quarter"`
	 Year                 string `json:"year"`
	 Proof				  string `json:"proof"`
 }

 func (s *StatementSmartContract) initLedger(APIstub shim.ChaincodeStubInterface) sc.Response {


	 statements := []Statement{
		 {
			Title:       "Báo cáo tài chính 2017 quý I ",
			Description: "Tình hình kinh doanh quý I của năm 2017",
			Quarter:     "1",
			Year:        "2017",
			Proof:		  proof,
		 },
		 {
			Title:       "Báo cáo tài chính 2017 quý II ",
			Description: "Tình hình kinh doanh quý II của năm 2017",
			Quarter:     "2",
			Year:        "2017",
			Proof:		  proof,
		 },
		 {
			Title:       "Báo cáo tài chính 2017 quý III ",
			Description: "Tình hình kinh doanh quý III của năm 2017",
			Quarter:     "3",
			Year:        "2017",
			Proof:		  proof,
		 },
		 {
			Title:       "Báo cáo tài chính 2017 quý IV ",
			Description: "Tình hình kinh doanh quý IV của năm 2017",
			Quarter:     "4",
			Year:        "2017",
			Proof:		  proof,
		 },
		 {
			Title:       "Báo cáo tài chính 2017 quý IV ",
			Description: "Tình hình kinh doanh quý IV của năm 2017",
			Quarter:     "5",
			Year:        "2017",
			Proof:		  proof,
		 },
		 {
			 Title:       "Báo cáo tài chính 2018 quý I ",
			 Description: "Tình hình kinh doanh quý I của năm 2018",
			 Quarter:     "1",
			 Year:        "2018",
			 Proof:		  proof,
		 },
		 {
			 Title:       "Báo cáo tài chính 2018 quý II ",
			 Description: "Tình hình kinh doanh quý II của năm 2018",
			 Quarter:     "2",
			 Year:        "2018",
			 Proof:		  proof,
		 },
		 {
			 Title:       "Báo cáo tài chính 2018 quý III ",
			 Description: "Tình hình kinh doanh quý III của năm 2018",
			 Quarter:     "3",
			 Year:        "2018",
			 Proof:		  proof,
		 },
		 {
			 Title:       "Báo cáo tài chính 2018 quý IV ",
			 Description: "Tình hình kinh doanh quý IV của năm 2018",
			 Quarter:     "4",
			 Year:        "2018",
			 Proof:		  proof,
		 },
		 {
			 Title:       "Báo cáo tài chính 2018 quý IV ",
			 Description: "Tình hình kinh doanh quý IV của năm 2018",
			 Quarter:     "5",
			 Year:        "2018",
			 Proof:		  proof,
		 },
		 {
			 Title:       "Báo cáo tài chính 2019 quý I ",
			 Description: "Tình hình kinh doanh quý I của năm 2019",
			 Quarter:     "1",
			 Year:        "2019",
			 Proof:		  proof,
		 },
		 {
			 Title:       "Báo cáo tài chính 2019 quý II ",
			 Description: "Tình hình kinh doanh quý II của năm 2019",
			 Quarter:     "2",
			 Year:        "2019",
			 Proof:		  proof,
		 },
		 {
			 Title:       "Báo cáo tài chính 2019 quý III ",
			 Description: "Tình hình kinh doanh quý III của năm 2019",
			 Quarter:     "3",
			 Year:        "2019",
			 Proof:		  proof,
		 },
		 {
			 Title:       "Báo cáo tài chính 2019 quý IV ",
			 Description: "Tình hình kinh doanh quý IV của năm 2019",
			 Quarter:     "4",
			 Year:        "2019",
			 Proof:		  proof,
		 },
		 {
			 Title:       "Báo cáo tài chính 2019 quý IV ",
			 Description: "Tình hình kinh doanh quý IV của năm 2019",
			 Quarter:     "5",
			 Year:        "2019",
			 Proof:		  proof,
		 },
	 }

	 i := 0
	 for i < len(statements) {
		 fmt.Println("i is ", i)
		 statementAsBytes, _ := json.Marshal(statements[i])
		 APIstub.PutState("statement:"+strconv.Itoa(i+1), statementAsBytes)
		 fmt.Println("Added", statements[i])
		 i = i + 1
	 }

	 return shim.Success(nil)
 }

 func (s *StatementSmartContract) queryStatement(APIstub shim.ChaincodeStubInterface, args []string) sc.Response {

	 if len(args) != 1 {
		 return shim.Error("Incorrect number of arguments. Expecting 1")
	 }

	 statementAsBytes, _ := APIstub.GetState(args[0])
	 return shim.Success(statementAsBytes)

 }

 func (s *StatementSmartContract) createStatement(APIstub shim.ChaincodeStubInterface, args []string) sc.Response {

	 if len(args) != 6 {
		 return shim.Error("Incorrect number of arguments. Expecting 6")
	 }

	 var statement = Statement{
		 Title:                      args[1],
		 Description:                args[2],
		 Quarter:                    args[3],
		 Year:                       args[4],
		 Proof:					     args[5],
	 }

	 statementAsBytes, _ := json.Marshal(statement)
	 APIstub.PutState(args[0], statementAsBytes)

	 return shim.Success(nil)
 }

 func (s *StatementSmartContract) queryAllStatements(APIstub shim.ChaincodeStubInterface) sc.Response {

	 fields := []string{"_id",
		 "title",
		 "description",
		 "quarter",
		 "year"}

	 query := map[string]interface{}{
		 "selector": map[string]interface{}{
			 "_id": map[string]interface{}{
				 "$regex": "^statement:",
			 },
		 },
		 "fields": fields,
	 }

	 queryBytes, err := json.Marshal(query)
	 if err != nil {
		 return shim.Error("error marshal")
	 }

	 //APIstub.GetQueryResult()
	 resultsIterator, err := APIstub.GetQueryResult(string(queryBytes))

	 if err != nil {
		 return shim.Error(err.Error())
	 }
	 defer resultsIterator.Close()

	 // buffer is a JSON array containing QueryResults
	 var buffer bytes.Buffer
	 buffer.WriteString("[")

	 bArrayMemberAlreadyWritten := false
	 for resultsIterator.HasNext() {
		 queryResponse, err := resultsIterator.Next()
		 if err != nil {
			 return shim.Error(err.Error())
		 }
		 // Add a comma before array members, suppress it for the first array member
		 if bArrayMemberAlreadyWritten == true {
			 buffer.WriteString(",")
		 }
		 buffer.WriteString("{\"Key\":")
		 buffer.WriteString("\"")
		 buffer.WriteString(queryResponse.Key)
		 buffer.WriteString("\"")

		 buffer.WriteString(", \"Record\":")
		 // Record is a JSON object, so we write as-is
		 buffer.WriteString(string(queryResponse.Value))
		 buffer.WriteString("}")
		 bArrayMemberAlreadyWritten = true
	 }
	 buffer.WriteString("]")

	 fmt.Printf("- queryAllFinStatements:\n%s\n", buffer.String())

	 return shim.Success(buffer.Bytes())

 }

 func (s *StatementSmartContract) queryAllStatementsByTime(APIstub shim.ChaincodeStubInterface, args []string) sc.Response {

	 if len(args) != 2 {
		 return shim.Error("Incorrect number of arguments. Expecting 1")
	 }

	 fields := []string{"_id",
		 "title",
		 "description",
		 "quarter",
		 "year"}

	 query := map[string]interface{}{
		 "selector": map[string]interface{}{
			 "_id": map[string]interface{}{
				 "$regex": "^statement:",
			 },
			 "quarter": map[string]interface{}{
				 "$regex": args[0],
			 },
			 "year": map[string]interface{}{
				 "$regex": args[1],
			 },
		 },
		 "fields": fields,
	 }

	 queryBytes, err := json.Marshal(query)
	 if err != nil {
		 return shim.Error("error marshal")
	 }

	 //APIstub.GetQueryResult()
	 resultsIterator, err := APIstub.GetQueryResult(string(queryBytes))

	 if err != nil {
		 return shim.Error(err.Error())
	 }
	 defer resultsIterator.Close()

	 // buffer is a JSON array containing QueryResults
	 var buffer bytes.Buffer
	 buffer.WriteString("[")

	 bArrayMemberAlreadyWritten := false
	 for resultsIterator.HasNext() {
		 queryResponse, err := resultsIterator.Next()
		 if err != nil {
			 return shim.Error(err.Error())
		 }
		 // Add a comma before array members, suppress it for the first array member
		 if bArrayMemberAlreadyWritten == true {
			 buffer.WriteString(",")
		 }
		 buffer.WriteString("{\"Key\":")
		 buffer.WriteString("\"")
		 buffer.WriteString(queryResponse.Key)
		 buffer.WriteString("\"")

		 buffer.WriteString(", \"Record\":")
		 // Record is a JSON object, so we write as-is
		 buffer.WriteString(string(queryResponse.Value))
		 buffer.WriteString("}")
		 bArrayMemberAlreadyWritten = true
	 }
	 buffer.WriteString("]")

	 fmt.Printf("- queryAllFinStatements:\n%s\n", buffer.String())

	 return shim.Success(buffer.Bytes())

 }

 func (s *StatementSmartContract) updateStatement(APIstub shim.ChaincodeStubInterface, args []string) sc.Response {

	 if len(args) != 6 {
		 return shim.Error("Incorrect number of arguments. Expecting 6")
	 }

	 statementAsBytes, _ := APIstub.GetState(args[0])

	 if statementAsBytes == nil {
		 return shim.Error("This statement does not exist in the state database")
	 }

	 statement := Statement{}
	 json.Unmarshal(statementAsBytes, &statement)

	 statement.Title = args[1]
	 statement.Description = args[2]
	 statement.Quarter = args[3]
	 statement.Year = args[4]
	 statement.Proof = args[5]

	 statementAsBytes, _ = json.Marshal(statement)
	 APIstub.PutState(args[0], statementAsBytes)

	 return shim.Success(nil)

 }

 func (s *StatementSmartContract) deleteStatement(APIstub shim.ChaincodeStubInterface, args []string) sc.Response {

	 if len(args) != 1 {
		 return shim.Error("Incorrect number of arguments. Expecting 2")
	 }

	 APIstub.DelState(args[0])

	 return shim.Success(nil)

 }

 func (s *StatementSmartContract) Init(APIstub shim.ChaincodeStubInterface) sc.Response {
	 return shim.Success(nil)
 }

 