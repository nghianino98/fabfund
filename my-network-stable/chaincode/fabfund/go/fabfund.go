package main

import (
	"fmt"
	"github.com/hyperledger/fabric-chaincode-go/shim"

	sc "github.com/hyperledger/fabric-protos-go/peer"
)

// Contract
var activitySmartContract = new(ActivitySmartContract)

var statementSmartContract = new(StatementSmartContract)
var cashFlowStatementSmartContract = new(CashFlowStatementSmartContract)
var financialStatementSmartContract = new(FinancialStatementSmartContract)
var incomesStatementSmartContract = new(IncomesStatementSmartContract)
var shareHolderStatementSmartContract = new(ShareHolderSmartContract)
var typeSmartContract = new(TypeSmartContract)

//
type FabFundSmartContract struct{}

func (s *FabFundSmartContract) Init(APIstub shim.ChaincodeStubInterface) sc.Response {
	return shim.Success(nil)
}

func (s *FabFundSmartContract) Invoke(APIstub shim.ChaincodeStubInterface) sc.Response {

	// Retrieve the requested Smart Contract function and arguments
	function, args := APIstub.GetFunctionAndParameters()
	// Route to the appropriate handler function to interact with the ledger appropriately
	if function == "initLedger" {
		return s.initLedger(APIstub)
	} else if function == "queryStatement" {
		return statementSmartContract.queryStatement(APIstub, args)
	} else if function == "createStatement" {
		return statementSmartContract.createStatement(APIstub, args)
	} else if function == "queryAllStatements" {
		return statementSmartContract.queryAllStatements(APIstub)
	} else if function == "queryAllStatementsByTime" {
		return statementSmartContract.queryAllStatementsByTime(APIstub, args)
	} else if function == "updateStatement" {
		return statementSmartContract.updateStatement(APIstub, args)
	} else if function == "deleteStatement" {
		return statementSmartContract.deleteStatement(APIstub, args)

	} else if function == "queryAllTypeLv2" {
		return typeSmartContract.queryAllType2(APIstub)
	} else if function == "queryAllTypeLv3ByLv3" {
		return typeSmartContract.queryType3ByType2(APIstub, args)
	} else if function == "createTypeLv2" {
		return typeSmartContract.createType2(APIstub, args)
	} else if function == "createTypeLv3" {
		return typeSmartContract.createType3(APIstub, args)

	} else if function == "queryCashFlowStatement" {
		return cashFlowStatementSmartContract.queryCashFlowStatement(APIstub, args)
	} else if function == "createCashFlowStatement" {
		return cashFlowStatementSmartContract.createCashFlowStatement(APIstub, args)
	} else if function == "queryAllCashFlowStatements" {
		return cashFlowStatementSmartContract.queryAllStatements(APIstub)
	} else if function == "queryAllCashFlowStatementsByTime" {
		return cashFlowStatementSmartContract.queryAllStatementsByTime(APIstub, args)
	//} else if function == "queryAllCashFlowStatementsByDate" {
	//	return cashFlowStatementSmartContract.queryAllStatementsByDate(APIstub, args)
	} else if function == "updateCashFlowStatement" {
		return cashFlowStatementSmartContract.updateCashFlowStatement(APIstub, args)
	} else if function == "deleteCashFlowStatement" {
		return cashFlowStatementSmartContract.deleteCashFlowStatement(APIstub, args)

	} else if function == "queryFinStatement" {
		return financialStatementSmartContract.queryFinancialStatement(APIstub, args)
	} else if function == "queryFinStatementOverview" {
		return financialStatementSmartContract.queryFinancialStatementForOverview(APIstub, args)
	} else if function == "createFinStatement" {
		return financialStatementSmartContract.createFinancialStatement(APIstub, args)
	} else if function == "queryAllFinStatements" {
		return financialStatementSmartContract.queryAllStatements(APIstub)
	} else if function == "queryAllFinStatementsByTime" {
		return financialStatementSmartContract.queryAllStatementsByTime(APIstub, args)
	//} else if function == "queryAllFinStatementsByDate" {
	//	return financialStatementSmartContract.queryAllStatementsByDate(APIstub, args)
	} else if function == "updateFinStatement" {
		return financialStatementSmartContract.updateFinancialStatement(APIstub, args)
	} else if function == "deleteFinStatement" {
		return financialStatementSmartContract.deleteFinancialStatement(APIstub, args)

	} else if function == "queryIncomesStatement" {
		return incomesStatementSmartContract.queryIncomesStatement(APIstub, args)
	} else if function == "queryIncomesOverview" {
		return incomesStatementSmartContract.queryIncomesStatementForOverview(APIstub, args)
	} else if function == "createIncomesStatement" {
		return incomesStatementSmartContract.createIncomesStatement(APIstub, args)
	} else if function == "queryAllIncomesStatements" {
		return incomesStatementSmartContract.queryAllStatements(APIstub)
	} else if function == "queryAllIncomesStatementsByTime" {
		return incomesStatementSmartContract.queryAllStatementsByTime(APIstub, args)
	//} else if function == "queryAllIncomesStatementsByDate" {
	//	return incomesStatementSmartContract.queryAllStatementsByDate(APIstub, args)
	} else if function == "updateIncomesStatement" {
		return incomesStatementSmartContract.updateIncomesStatement(APIstub, args)
	} else if function == "deleteIncomesStatement" {
		return incomesStatementSmartContract.deleteIncomesStatement(APIstub, args)

		//
	//} else if function == "queryShareholder" {
	//	return shareHolderStatementSmartContract.queryShareholder(APIstub, args)
	//} else if function == "createShareholder" {
	//	return shareHolderStatementSmartContract.createShareholder(APIstub, args)
	//} else if function == "queryAllShareholders" {
	//	return shareHolderStatementSmartContract.queryAllShareholder(APIstub)
	//} else if function == "updateShareholder" {
	//	return shareHolderStatementSmartContract.updateShareholder(APIstub, args)
	//} else if function == "deleteShareholder" {
	//	return shareHolderStatementSmartContract.deleteShareholder(APIstub, args)

		//	Activity
	} else if function == "queryActivity" {
		return activitySmartContract.queryActivity(APIstub, args)
	} else if function == "createActivity" {
		return activitySmartContract.createActivity(APIstub, args)
	} else if function == "queryAllActivities" {
		return activitySmartContract.queryAllActivities(APIstub)
	} else if function == "queryAllActivitiesByDate" {
		return activitySmartContract.queryAllActivitiesByDate(APIstub,args)
	} else if function == "queryAllActivitiesByType" {
		return activitySmartContract.queryAllActivitiesByType(APIstub,args)
	} else if function == "queryAllActivitiesByActorIdByApprovedId" {
		return activitySmartContract.queryAllActivitiesByActorIdByApprovedId(APIstub,args)
	} else if function == "queryAllActivitiesByActorApprovedId" {
		return activitySmartContract.queryAllActivitiesByActorApprovedId(APIstub,args)
	} else if function == "updateActivity" {
		return activitySmartContract.updateActivity(APIstub, args)
	} else if function == "deleteActivity" {
		return activitySmartContract.deleteActivity(APIstub, args)

		//
	} else if function == "queryHistory" {
		return activitySmartContract.queryHistoryKey(APIstub, args)
	}

	return shim.Error("Invalid Smart Contract function name.")
}

func (s *FabFundSmartContract) initLedger(APIstub shim.ChaincodeStubInterface) sc.Response {
	activitySmartContract.initLedger(APIstub)
	cashFlowStatementSmartContract.initLedger(APIstub)
	financialStatementSmartContract.initLedger(APIstub)
	incomesStatementSmartContract.initLedger(APIstub)
	shareHolderStatementSmartContract.initLedger(APIstub)
	statementSmartContract.initLedger(APIstub)
	typeSmartContract.initLedger(APIstub)
	return shim.Success(nil)
}

// The main function is only relevant in unit test mode. Only included here for completeness.
func main() {

	// Create a new Smart Contract
	err := shim.Start(new(FabFundSmartContract))
	if err != nil {
		fmt.Printf("Error creating new FabFundSmartContract: %s", err)
	}
}
