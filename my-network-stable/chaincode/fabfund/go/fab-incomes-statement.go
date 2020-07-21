/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

/*
 * The sample smart contract for documentation topic:
 * Writing Your First Blockchain Application
 */

package main

/* Imports
 * 4 utility libraries for formatting, handling bytes, reading and writing JSON, and string manipulation
 * 2 specific Hyperledger Fabric specific libraries for Smart Contracts
 */
import (
	"bytes"
	"encoding/json"
	"fmt"
	"github.com/hyperledger/fabric-chaincode-go/shim"

	sc "github.com/hyperledger/fabric-protos-go/peer"
	"strconv"
)

// Define the Smart Contract structure
type IncomesStatementSmartContract struct {
}

// Define the Incomes Statement
type IncomesStatement struct {
	TotalRevenue     string `json:"totalRevenue"`
	CoreRevenue      string `json:"coreRevenue"`
	FinancialRevenue string `json:"financialRevenue"`
	OtherRevenue     string `json:"otherRevenue"`
	ProfitBeforeTax  string `json:"profitBeforeTax"`
	ProfitAfterTax   string `json:"profitAfterTax"`
	ProofImage       string `json:"proofImage"`
	CreateAt         string `json:"createAt"`
	LastCreateAt     string `json:"lastCreateAt"`
	Quarter					 string `json:"quarter"`
	Year					 string `json:"year"`
}

/*
* The Init method is called when the Smart Contract "IncomesStatement" is instantiated by the blockchain network
* Best practice is to have any Ledger initialization in separate function -- see initLedger()
 */
func (s *IncomesStatementSmartContract) Init(APIstub shim.ChaincodeStubInterface) sc.Response {
	return shim.Success(nil)
}

func (s *IncomesStatementSmartContract) initLedger(APIstub shim.ChaincodeStubInterface) sc.Response {


	statements := []IncomesStatement{
		{
			TotalRevenue:     "893173592864",
			CoreRevenue:      "876678499352",
			FinancialRevenue: "15783409071",
			OtherRevenue:     "711684441",
			ProfitBeforeTax:  "292590594467",
			ProfitAfterTax:   "234072477237",
			ProofImage:       proofimage,
			CreateAt:         "1553990400000",
			LastCreateAt:     "0",
			Quarter:		"1",
			Year:"2017",
		},
		{
			TotalRevenue:     "840942110171",
			CoreRevenue:      "822084274693",
			FinancialRevenue: "18816496412",
			OtherRevenue:     "41339066",
			ProfitBeforeTax:  "191994505980",
			ProfitAfterTax:   "151216926561",
			ProofImage:       proofimage,
			CreateAt:         "1561852800000",
			LastCreateAt:     "0",
			Quarter:		"2",
			Year:"2017",
		},
		{
			TotalRevenue:     "852499328650",
			CoreRevenue:      "829115275445",
			FinancialRevenue: "23312865437",
			OtherRevenue:     "71187768",
			ProfitBeforeTax:  "181583155283",
			ProfitAfterTax:   "145431701548",
			ProofImage:       proofimage,
			CreateAt:         "1569801600000",
			LastCreateAt:     "0",
			Quarter:		"3",
			Year:"2017",
		},
		{
			TotalRevenue:     "806899596716",
			CoreRevenue:      "777877575698",
			FinancialRevenue: "28955650745",
			OtherRevenue:     "66370273",
			ProfitBeforeTax:  "33970691090",
			ProfitAfterTax:   "32465526505",
			ProofImage:       proofimage,
			CreateAt:         "1577750400000",
			LastCreateAt:     "0",
			Quarter:		"4",
			Year:"2017",
		},
		{
			TotalRevenue:     "3402524148590",
			CoreRevenue:      "3314107204063",
			FinancialRevenue: "86867934111",
			OtherRevenue:     "1549010416",
			ProfitBeforeTax:  "647520498018",
			ProfitAfterTax:   "514032019563",
			ProofImage:       proofimage,
			CreateAt:         "1577750400000",
			LastCreateAt:     "0",
			Quarter:		"5",
			Year:"2017",
		},
		{
			TotalRevenue:     "782185388716",
			CoreRevenue:      "759885326512",
			FinancialRevenue: "22177845048",
			OtherRevenue:     "122217156",
			ProfitBeforeTax:  "106370009989",
			ProfitAfterTax:   "84872215043",
			ProofImage:       proofimage,
			CreateAt:         "1553990400000",
			LastCreateAt:     "0",
			Quarter:		"1",
			Year:"2018",
		},
		{
			TotalRevenue:     "740797845354",
			CoreRevenue:      "717658445329",
			FinancialRevenue: "19375197236",
			OtherRevenue:     "3764202789",
			ProfitBeforeTax:  "21148206880",
			ProfitAfterTax:   "16028345047",
			ProofImage:       proofimage,
			CreateAt:         "1561852800000",
			LastCreateAt:     "0",
			Quarter:		"2",
			Year:"2018",
		},
		{
			TotalRevenue:     "2353903353824",
			CoreRevenue:      "2276591788571",
			FinancialRevenue: "72228893411",
			OtherRevenue:     "5082671842",
			ProfitBeforeTax:  "158243134157",
			ProfitAfterTax:   "113423278359",
			ProofImage:       proofimage,
			CreateAt:         "1569801600000",
			LastCreateAt:     "0",
			Quarter:		"3",
			Year:"2018",
		},
		{
			TotalRevenue:     "895681613843",
			CoreRevenue:      "847776171497",
			FinancialRevenue: "46402552341",
			OtherRevenue:     "1502890005",
			ProfitBeforeTax:  "138642132155",
			ProfitAfterTax:   "103293238319",
			ProofImage:       proofimage,
			CreateAt:         "1577750400000",
			LastCreateAt:     "0",
			Quarter:		"4",
			Year:"2018",
		},
		{
			TotalRevenue:     "3245711872947",
			CoreRevenue:      "3124367960068",
			FinancialRevenue: "118622660093",
			OtherRevenue:     "2721252786",
			ProfitBeforeTax:  "71245241314",
			ProfitAfterTax:   "60845792951",
			ProofImage:       proofimage,
			CreateAt:         "1577750400000",
			LastCreateAt:     "0",
			Quarter:		"5",
			Year:"2018",
		},
		{
			TotalRevenue:     "880344032358",
			CoreRevenue:      "863970706716",
			FinancialRevenue: "16131356616",
			OtherRevenue:     "241969026",
			ProfitBeforeTax:  "40505637488",
			ProfitAfterTax:   "32301570206",
			ProofImage:       proofimage,
			CreateAt:         "1553990400000",
			LastCreateAt:     "0",
			Quarter:		"1",
			Year:"2019",
		},
		{
			TotalRevenue:     "880344032358",
			CoreRevenue:      "863970706716",
			FinancialRevenue: "16131356616",
			OtherRevenue:     "241969026",
			ProfitBeforeTax:  "40505637488",
			ProfitAfterTax:   "32301570206",
			ProofImage:       proofimage,
			CreateAt:         "1561852800000",
			LastCreateAt:     "0",
			Quarter:		"2",
			Year:"2019",
		},
		{
			TotalRevenue:     "880344032358",
			CoreRevenue:      "863970706716",
			FinancialRevenue: "16131356616",
			OtherRevenue:     "241969026",
			ProfitBeforeTax:  "40505637488",
			ProfitAfterTax:   "32301570206",
			ProofImage:       proofimage,
			CreateAt:         "1569801600000",
			LastCreateAt:     "0",
			Quarter:		"3",
			Year:"2019",
		},
		{
			TotalRevenue:     "965611382251",
			CoreRevenue:      "941425522187",
			FinancialRevenue: "23146772478",
			OtherRevenue:     "1039087586",
			ProfitBeforeTax:  "112763803422",
			ProfitAfterTax:   "81099908788",
			ProofImage:       proofimage,
			CreateAt:         "1577750400000",
			LastCreateAt:     "0",
			Quarter:		"4",
			Year:"2019",
		},
		{
			TotalRevenue:     "3775262680189",
			CoreRevenue:      "3690780773282",
			FinancialRevenue: "81419248272",
			OtherRevenue:     "3062658635",
			ProfitBeforeTax:  "7744212103",
			ProfitAfterTax:   "183069119",
			ProofImage:       proofimage,
			CreateAt:         "1577750400000",
			LastCreateAt:     "0",
			Quarter:		"5",
			Year:"2019",
		},
	}

	i := 0
	for i < len(statements) {
		fmt.Println("i is ", i)
		statementAsBytes, _ := json.Marshal(statements[i])
		APIstub.PutState("istatement:"+strconv.Itoa(i+1), statementAsBytes)
		fmt.Println("Added", statements[i])
		i = i + 1
	}

	return shim.Success(nil)
}

func (s *IncomesStatementSmartContract) queryIncomesStatement(APIstub shim.ChaincodeStubInterface, args []string) sc.Response {

	if len(args) != 1 {
		return shim.Error("Incorrect number of arguments. Expecting 1")
	}

	statementAsBytes, _ := APIstub.GetState(args[0])
	return shim.Success(statementAsBytes)

}

func (s *IncomesStatementSmartContract) queryIncomesStatementForOverview(APIstub shim.ChaincodeStubInterface, args []string) sc.Response {

	if len(args) != 1 {
		return shim.Error("Incorrect number of arguments. Expecting 1")
	}

	fields := []string{
		"totalRevenue",
	}

	query := map[string]interface{}{
		"selector": map[string]interface{}{
			"_id": map[string]interface{}{
				"$regex": args[0],
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

func (s *IncomesStatementSmartContract) createIncomesStatement(APIstub shim.ChaincodeStubInterface, args []string) sc.Response {

	if len(args) != 12 {
		return shim.Error("Incorrect number of arguments. Expecting 12")
	}

	var statement = IncomesStatement{
		TotalRevenue:     args[1],
		CoreRevenue:      args[2],
		FinancialRevenue: args[3],
		OtherRevenue:     args[4],
		ProfitBeforeTax:  args[5],
		ProfitAfterTax:   args[6],
		ProofImage:       args[7],
		CreateAt:         args[8],
		LastCreateAt:     args[9],
		Quarter:		  args[10],
		Year:			  args[11],
	}

	statementAsBytes, _ := json.Marshal(statement)
	APIstub.PutState(args[0], statementAsBytes)

	return shim.Success(nil)
}

func (s *IncomesStatementSmartContract) queryAllStatements(APIstub shim.ChaincodeStubInterface) sc.Response {

	query := map[string]interface{}{
		"selector": map[string]interface{}{
			"_id": map[string]interface{}{
				"$regex": "^istatement:",
			},
		},
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

	fmt.Printf("- queryAllIncomesStatements:\n%s\n", buffer.String())

	return shim.Success(buffer.Bytes())

}

func (s *IncomesStatementSmartContract) queryAllStatementsByTime(APIstub shim.ChaincodeStubInterface, args[]string) sc.Response {

	if len(args) != 4 {
		return shim.Error("Incorrect number of arguments. Expecting 4")
	}

	fields := []string{
		"_id",
		"totalRevenue",
		"coreRevenue",
		"financialRevenue",
		"otherRevenue",
		"profitBeforeTax",
		"profitAfterTax",
		"createAt",
		"lastCreateAt",
		"quarter",
		"year"}

	query := map[string]interface{}{
		"selector": map[string]interface{}{
			"_id": map[string]interface{}{
				"$regex": "^istatement:",
			},
			"quarter": map[string]interface{}{
				"$regex": args[0],
			},
			"year": map[string]interface{}{
				"$regex": args[1],
			},
			"createAt": map[string]interface{}{
				"$gte": args[2],
				"$lte": args[3],
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

	fmt.Printf("- queryAllIncomesStatements:\n%s\n", buffer.String())

	return shim.Success(buffer.Bytes())

}

//func (s *IncomesStatementSmartContract) queryAllStatementsByDate(APIstub shim.ChaincodeStubInterface, args[]string) sc.Response {
//
//	if len(args) != 2 {
//		return shim.Error("Incorrect number of arguments. Expecting 2")
//	}
//
//	fields := []string{
//		"_id",
//		"totalRevenue",
//		"coreRevenue",
//		"financialRevenue",
//		"otherRevenue",
//		"profitBeforeTax",
//		"profitAfterTax",
//		"createAt",
//		"lastCreateAt",
//		"quarter",
//		"year"}
//
//	query := map[string]interface{}{
//		"selector": map[string]interface{}{
//			"_id": map[string]interface{}{
//				"$regex": "^istatement:",
//			},
//			"createAt": map[string]interface{}{
//				"$gte": args[0],
//				"$lt": args[1],
//			},
//		},
//		"fields": fields,
//	}
//	queryBytes, err := json.Marshal(query)
//	if err != nil {
//		return shim.Error("error marshal")
//	}
//
//	//APIstub.GetQueryResult()
//	resultsIterator, err := APIstub.GetQueryResult(string(queryBytes))
//	if err != nil {
//		return shim.Error(err.Error())
//	}
//	defer resultsIterator.Close()
//
//	// buffer is a JSON array containing QueryResults
//	var buffer bytes.Buffer
//	buffer.WriteString("[")
//
//	bArrayMemberAlreadyWritten := false
//	for resultsIterator.HasNext() {
//		queryResponse, err := resultsIterator.Next()
//		if err != nil {
//			return shim.Error(err.Error())
//		}
//		// Add a comma before array members, suppress it for the first array member
//		if bArrayMemberAlreadyWritten == true {
//			buffer.WriteString(",")
//		}
//		buffer.WriteString("{\"Key\":")
//		buffer.WriteString("\"")
//		buffer.WriteString(queryResponse.Key)
//		buffer.WriteString("\"")
//
//		buffer.WriteString(", \"Record\":")
//		// Record is a JSON object, so we write as-is
//		buffer.WriteString(string(queryResponse.Value))
//		buffer.WriteString("}")
//		bArrayMemberAlreadyWritten = true
//	}
//	buffer.WriteString("]")
//
//	fmt.Printf("- queryAllIncomesStatements:\n%s\n", buffer.String())
//
//	return shim.Success(buffer.Bytes())
//
//}

func (s *IncomesStatementSmartContract) updateIncomesStatement(APIstub shim.ChaincodeStubInterface, args []string) sc.Response {

	if len(args) != 12 {
		return shim.Error("Incorrect number of arguments. Expecting 12")
	}

	statementAsBytes, _ := APIstub.GetState(args[0])

	if statementAsBytes == nil {
		return shim.Error("This statement does not exist in the state database")
	}

	statement := IncomesStatement{}
	json.Unmarshal(statementAsBytes, &statement)

	statement.TotalRevenue = args[1]
	statement.CoreRevenue = args[2]
	statement.FinancialRevenue = args[3]
	statement.OtherRevenue = args[4]
	statement.ProfitBeforeTax = args[5]
	statement.ProfitAfterTax = args[6]
	statement.ProofImage = args[7]
	statement.CreateAt = args[8]
	statement.LastCreateAt = args[9]
	statement.Quarter = args[11]
	statement.Year = args[12]

	statementAsBytes, _ = json.Marshal(statement)
	APIstub.PutState(args[0], statementAsBytes)

	return shim.Success(nil)

}

func (s *IncomesStatementSmartContract) deleteIncomesStatement(APIstub shim.ChaincodeStubInterface, args []string) sc.Response {

	if len(args) != 1 {
		return shim.Error("Incorrect number of arguments. Expecting 2")
	}

	APIstub.DelState(args[0])

	return shim.Success(nil)

}